package org.dyno.visual.swing.parser.adapters;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.EventSetDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.parser.ParserPlugin;
import org.dyno.visual.swing.parser.spi.IParser;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.IAdapterContext;
import org.dyno.visual.swing.plugin.spi.IConstants;
import org.dyno.visual.swing.plugin.spi.IEventListenerModel;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.ui.refactoring.RenameSupport;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class WidgetParser implements IParser, IConstants, IAdapterContext {
	protected WidgetAdapter adapter;

	protected IJavaElement getSibling(IType type, IJavaElement element) {
		try {
			IJavaElement[] children = type.getChildren();
			for (int i = 0; i < children.length; i++) {
				if (children[i].equals(element) && i < children.length - 1) {
					return children[i + 1];
				}
			}
		} catch (JavaModelException e) {
			ParserPlugin.getLogger().error(e);
		}
		return null;
	}

	private IJavaElement getInitMethodSibling(IType type) {
		String initMethodName = "initComponent";
		IMethod method = type.getMethod(initMethodName, new String[0]);
		if (method != null && method.exists()) {
			return getSibling(type, method);
		}
		return null;
	}

	public boolean generateCode(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
		if (!adapter.isDirty())
			return true;
		if (adapter.isRoot()) {
			return createRootCode(type, imports, monitor);
		} else {
			return createNonRootCode(type, imports, monitor);
		}
	}

	private boolean createNonRootCode(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
		boolean success = true;
		String lastName = adapter.getLastName();
		String name = adapter.getName();
		if (lastName != null && !lastName.equals(name)) {
			IField lastField = type.getField(getFieldName(lastName));
			try {
				int flags = RenameSupport.UPDATE_GETTER_METHOD
						| RenameSupport.UPDATE_REFERENCES
						| RenameSupport.UPDATE_SETTER_METHOD;
				RenameSupport rs = RenameSupport.create(lastField, name, flags);
				if (rs.preCheck().isOK()) {
					IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					Shell parent = window.getShell();
					rs.perform(parent, window);
				}
			} catch (Exception e) {
				ParserPlugin.getLogger().error(e);
			}
		} else if (lastName==null){
			IField field = type.getField(getFieldName(name));
			if (field != null && !field.exists()) {
				StringBuilder builder = new StringBuilder();
				builder.append(getAccessCode(adapter.getFieldAccess()));
				builder.append(" ");
				String fqcn = adapter.getWidgetCodeClassName();
				String beanName = imports.addImport(fqcn);
				builder.append(beanName);
				builder.append(" ");
				builder.append(getFieldName(adapter.getName()));
				builder.append(";\n");
				try {
					type.createField(builder.toString(), null, false, monitor);
				} catch (JavaModelException e) {
					ParserPlugin.getLogger().error(e);
					success = false;
				}

			}
		}
		IJavaElement sibling = null;
		StringBuilder builder = new StringBuilder();
		String getMethodName = NamespaceManager.getInstance().getGetMethodName(name);
		IMethod method = type.getMethod(getMethodName, new String[0]);
		if (method != null && method.exists()) {
			try {
				sibling = getSibling(type, method);
				method.delete(false, monitor);				
			} catch (JavaModelException e) {
				ParserPlugin.getLogger().error(e);
				success = false;
			}
		}
		builder.append(getAccessCode(adapter.getGetAccess()));
		builder.append(" ");
		String fqcn = adapter.getWidgetCodeClassName();
		String beanName = imports.addImport(fqcn);
		builder.append(beanName);
		builder.append(" ");
		builder.append(getMethodName);
		builder.append("(){\n");
		builder.append("if(");
		builder.append(getFieldName(adapter.getName()));
		builder.append("==null){\n");
		builder.append(createGetCode(imports));
		builder.append("}\n");
		builder.append("return ");
		builder.append(getFieldName(adapter.getName()));
		builder.append(";\n");
		builder.append("}\n");
		try {
			if (sibling == null)
				sibling = getInitMethodSibling(type);
			type.createMethod(JavaUtil.formatCode(builder.toString()), sibling, false, monitor);
		} catch (JavaModelException e) {
			ParserPlugin.getLogger().error(e);
			success = false;
		}
		success = createEventMethod(type, imports, monitor);
		adapter.setLastName(adapter.getName());
		return success;
	}

	private boolean createEventMethod(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
		boolean success = true;
		Set<EventSetDescriptor> keySet = adapter.getEventDescriptor().keySet();
		if (!keySet.isEmpty()) {
			for (EventSetDescriptor eventSet : keySet) {
				IEventListenerModel model = adapter.getEventDescriptor().get(
						eventSet);
				success = model.createEventMethod(type, imports, monitor);
			}
		}
		return success;
	}

	private boolean createRootCode(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
		boolean success = true;
		String initMethodName = "initComponent";
		IMethod method = type.getMethod(initMethodName, new String[0]);
		IJavaElement sibling = null;
		if (method != null && method.exists()) {
			try {
				sibling = getSibling(type, method);
				method.delete(false, monitor);
			} catch (JavaModelException e) {
				ParserPlugin.getLogger().error(e);
				success = false;
			}
		}
		StringBuilder builder = new StringBuilder();
		builder.append("private void ");
		builder.append(initMethodName);
		builder.append("(){\n");
		builder.append(createInitCode(imports));
		for (InvisibleAdapter invisible : adapter.getInvisibles()) {
			builder.append(invisible.getCreationMethodName() + "();\n");
		}
		createPostInitCode(builder, imports);
		builder.append("}\n");
		try {
			type.createMethod(JavaUtil.formatCode(builder.toString()), sibling,
					false, monitor);
		} catch (JavaModelException e) {
			ParserPlugin.getLogger().error(e);
			success = false;
		}
		for (InvisibleAdapter invisible : adapter.getInvisibles()) {
			IParser parser = (IParser) invisible.getAdapter(IParser.class);
			if (parser != null)
				parser.generateCode(type, imports, monitor);
		}
		success = createEventMethod(type, imports, monitor);
		success = createConstructor(type, imports, monitor);
		return success;
	}

	protected void createPostInitCode(StringBuilder builder,
			ImportRewrite imports) {
		Dimension size = adapter.getWidget().getSize();
		builder.append("setSize(" + size.width + ", " + size.height + ");\n");
	}

	protected boolean createConstructor(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
		return true;
	}

	protected String getFieldName(String lastName) {
		return NamespaceManager.getInstance().getFieldName(lastName);
	}

	protected String createGetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(getFieldName(adapter.getName()) + " = "
				+ getNewInstanceCode(imports) + ";\n");
		builder.append(createSetCode(imports));
		System.out.println();
		CompositeAdapter conAdapter = adapter.getParentAdapter();
		if (conAdapter.needGenBoundCode()) {
			Rectangle bounds = adapter.getWidget().getBounds();
			String strBounds = getFieldName(adapter.getName()) + ".setBounds("
					+ bounds.x + ", " + bounds.y + ", " + bounds.width + ", "
					+ bounds.height + ");\n";
			builder.append(strBounds);
		}
		builder.append(genAddEventCode(imports));
		return builder.toString();
	}

	private String createSetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		ArrayList<IWidgetPropertyDescriptor> properties = adapter
				.getPropertyDescriptors();
		for (IWidgetPropertyDescriptor property : properties) {
			if (property.isPropertySet(adapter.getLnfClassname(),
					new StructuredSelection(adapter.getWidget()))
					&& (property.isGencode() || property.isEdited(adapter))) {
				String setCode = property.getSetCode(adapter.getWidget(),
						imports);
				if (setCode != null)
					builder.append(setCode);
			}
		}
		return builder.toString();
	}

	private String genAddEventCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		Set<EventSetDescriptor> keySet = adapter.getEventDescriptor().keySet();
		if (!keySet.isEmpty()) {
			for (EventSetDescriptor eventSet : keySet) {
				if (!adapter.isRoot())
					builder.append(getFieldName(adapter.getName()) + ".");
				Method mAdd = eventSet.getAddListenerMethod();
				builder.append(mAdd.getName() + "(");
				IEventListenerModel model = adapter.getEventDescriptor().get(
						eventSet);
				String newcode = model.createListenerInstance(imports);
				builder.append(newcode);
				builder.append(");\n");
			}
		}
		return builder.toString();
	}

	protected String createInitCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(createSetCode(imports));
		String code = genAddEventCode(imports);
		builder.append(code);
		return builder.toString();
	}

	protected String getNewInstanceCode(ImportRewrite imports) {
		String beanName = imports.addImport(adapter.getWidgetCodeClassName());
		return "new " + beanName + "()";
	}

	protected String getAccessCode(int access) {
		switch (access) {
		case ACCESS_PRIVATE:
			return "private";
		case ACCESS_PROTECTED:
			return "protected";
		case ACCESS_PUBLIC:
			return "public";
		}
		return "";
	}

	@Override
	public void setAdapter(IAdapter adapter) {
		this.adapter = (WidgetAdapter) adapter;
	}

}
