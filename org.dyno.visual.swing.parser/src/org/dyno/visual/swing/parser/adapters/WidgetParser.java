/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/
package org.dyno.visual.swing.parser.adapters;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.EventSetDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.parser.NamespaceUtil;
import org.dyno.visual.swing.parser.ParserPlugin;
import org.dyno.visual.swing.parser.spi.IParser;
import org.dyno.visual.swing.parser.spi.IPropertyCodeGenerator;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.IConstants;
import org.dyno.visual.swing.plugin.spi.IEventListenerModel;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelStatus;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.ui.refactoring.RenameSupport;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

public class WidgetParser implements IParser, IConstants, IAdaptableContext {
	protected WidgetAdapter adaptable;

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
		IMethod method = type.getMethod(INIT_METHOD_NAME, new String[0]);
		if (method != null && method.exists()) {
			return getSibling(type, method);
		}
		return null;
	}

	public boolean generateCode(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		if (!adaptable.isDirty())
			return true;
		Component widget = adaptable.getWidget();
		if (widget instanceof JComponent) {
			JComponent jcomp = (JComponent) widget;
			JPopupMenu jpm = JavaUtil.getComponentPopupMenu(jcomp);
			if (jpm != null && WidgetAdapter.getWidgetAdapter(jpm) != null) {
				WidgetAdapter jpmAdapter = WidgetAdapter.getWidgetAdapter(jpm);
				IParser parser = (IParser) jpmAdapter.getAdapter(IParser.class);
				if (parser != null) {
					parser.generateCode(type, imports, monitor);
				}
			}
		}
		if (adaptable.isRoot()) {
			return createRootCode(type, imports, monitor);
		} else {
			return createNonRootCode(type, imports, monitor);
		}
	}

	
	public boolean renameField(IType type, IProgressMonitor monitor) {
		String lastName = adaptable.getLastName();
		String name = adaptable.getName();
		if (lastName != null && !lastName.equals(name)) {
			IField lastField = type.getField(lastName);
			try {
				int flags = RenameSupport.UPDATE_GETTER_METHOD | RenameSupport.UPDATE_REFERENCES | RenameSupport.UPDATE_SETTER_METHOD;
				RenameSupport rs = RenameSupport.create(lastField, name, flags);
				if (rs.preCheck().isOK()) {
					IWorkbenchWindow window = JavaUtil.getEclipseWindow();
					Shell parent = JavaUtil.getEclipseShell();
					rs.perform(parent, window);
					adaptable.setLastName(name);
					return true;
				}
			} catch (JavaModelException jme) {
				IJavaModelStatus status = jme.getJavaModelStatus();
				if (!status.isDoesNotExist()) {
					ParserPlugin.getLogger().error(jme);
				} else {
					return true;
				}
			} catch (Exception e) {
				ParserPlugin.getLogger().error(e);
			}
			return false;
		} else
			return true;
	}

	private boolean createNonRootCode(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		String name = adaptable.getID();
		IField field = type.getField(name);
		if (field != null) {
			if (!field.exists()) {
				if (!createField(type, imports, monitor))
					return false;
			} else {
				try {
					int flags = field.getFlags();
					int access_code = getAccessModifier(flags);
					if (adaptable.getFieldAccess() != access_code) {
						field.delete(true, monitor);
						if (!createField(type, imports, monitor))
							return false;
					}
				} catch (Exception e) {
					ParserPlugin.getLogger().error(e);
					return false;
				}
			}
		} else {
			if (!createField(type, imports, monitor))
				return false;
		}
		IJavaElement sibling = null;
		String mName = NamespaceUtil.getGetMethodName(adaptable, name);
		IMethod method = type.getMethod(mName, new String[0]);
		if (method != null && method.exists()) {
			try {
				sibling = getSibling(type, method);
				method.delete(false, monitor);
			} catch (JavaModelException e) {
				ParserPlugin.getLogger().error(e);
				return false;
			}
		}
		if (!createGetMethod(type, imports, monitor, sibling))
			return false;
		if (!createEventMethod(type, imports, monitor))
			return false;
		return true;
	}

	private boolean createGetMethod(IType type, ImportRewrite imports, IProgressMonitor monitor, IJavaElement sibling) {
		String id = adaptable.getID();
		String getMethodName = NamespaceUtil.getGetMethodName(adaptable, id);
		StringBuilder builder = new StringBuilder();
		builder.append(getAccessCode(adaptable.getGetAccess()));
		builder.append(" ");
		String fqcn = adaptable.getWidgetCodeClassName();
		String beanName = imports.addImport(fqcn);
		builder.append(beanName);
		builder.append(" ");
		builder.append(getMethodName);
		builder.append("(){\n");
		builder.append("if(");
		builder.append(id);
		builder.append("==null){\n");
		builder.append(createGetCode(imports));
		builder.append("}\n");
		builder.append("return ");
		builder.append(id);
		builder.append(";\n");
		builder.append("}\n");
		try {
			if (sibling == null)
				sibling = getInitMethodSibling(type);
			type.createMethod(JavaUtil.formatCode(builder.toString()), sibling, false, monitor);
			return true;
		} catch (JavaModelException e) {
			ParserPlugin.getLogger().error(e);
			return false;
		}
	}

	private boolean createField(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		StringBuilder builder = new StringBuilder();
		builder.append(getAccessCode(adaptable.getFieldAccess()));
		builder.append(" ");
		String fqcn = adaptable.getWidgetCodeClassName();
		String beanName = imports.addImport(fqcn);
		builder.append(beanName);
		builder.append(" ");
		String id = adaptable.getID();
		builder.append(id);
		builder.append(";\n");
		try {
			type.createField(builder.toString(), null, false, monitor);
			return true;
		} catch (JavaModelException e) {
			ParserPlugin.getLogger().error(e);
			return false;
		}
	}

	private int getAccessModifier(int flags) {
		int access_code = ACCESS_PRIVATE;
		if (Flags.isPublic(flags)) {
			access_code = ACCESS_PUBLIC;
		} else if (Flags.isProtected(flags)) {
			access_code = ACCESS_PROTECTED;
		} else if (Flags.isPackageDefault(flags)) {
			access_code = ACCESS_DEFAULT;
		} else if (Flags.isPrivate(flags)) {
			access_code = ACCESS_PRIVATE;
		}
		return access_code;
	}

	private boolean createEventMethod(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		boolean success = true;
		Set<EventSetDescriptor> keySet = adaptable.getEventDescriptor().keySet();
		if (!keySet.isEmpty()) {
			for (EventSetDescriptor eventSet : keySet) {
				IEventListenerModel model = adaptable.getEventDescriptor().get(eventSet);
				success = model.createEventMethod(type, imports, monitor);
			}
		}
		return success;
	}

	private boolean createRootCode(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		IMethod method = type.getMethod(INIT_METHOD_NAME, new String[0]);
		IJavaElement sibling = null;
		if (method != null && method.exists()) {
			try {
				sibling = getSibling(type, method);
				method.delete(false, monitor);
			} catch (JavaModelException e) {
				ParserPlugin.getLogger().error(e);
				return false;
			}
		}
		if (!createInitMethod(type, imports, monitor, sibling))
			return false;
		for (InvisibleAdapter invisible : adaptable.getInvisibles()) {
			IParser parser = (IParser) invisible.getAdapter(IParser.class);
			if (parser != null) {
				if (!parser.generateCode(type, imports, monitor))
					return false;
			}
		}
		if (!createEventMethod(type, imports, monitor))
			return false;
		if (!createConstructor(type, imports, monitor))
			return false;
		return true;
	}

	private boolean createInitMethod(IType type, ImportRewrite imports, IProgressMonitor monitor, IJavaElement sibling) {
		StringBuilder builder = new StringBuilder();
		builder.append("private void ");
		builder.append(INIT_METHOD_NAME);
		builder.append("(){\n");
		builder.append(createInitCode(imports));
		for (InvisibleAdapter invisible : adaptable.getInvisibles()) {
			IParser invisibleParser = (IParser) invisible.getAdapter(IParser.class);
			builder.append(invisibleParser.getCreationMethodName() + "();\n");
		}
		createPostInitCode(builder, imports);
		builder.append("}\n");
		try {
			type.createMethod(JavaUtil.formatCode(builder.toString()), sibling, false, monitor);
		} catch (JavaModelException e) {
			ParserPlugin.getLogger().error(e);
			return false;
		}
		return true;
	}

	protected void createPostInitCode(StringBuilder builder, ImportRewrite imports) {
		Dimension size = adaptable.getWidget().getSize();
		builder.append("setSize(" + size.width + ", " + size.height + ");\n");
	}

	protected boolean createConstructor(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		return true;
	}

	protected String createGetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		String id = adaptable.getID();
		builder.append(id + " = " + getNewInstanceCode(imports) + ";\n");
		builder.append(createSetCode(imports));
		CompositeAdapter conAdapter = adaptable.getParentAdapter();
		if (conAdapter.needGenBoundCode()) {
			Rectangle bounds = adaptable.getWidget().getBounds();
			String strBounds = id + ".setBounds(" + bounds.x + ", " + bounds.y + ", " + bounds.width + ", " + bounds.height + ");\n";
			builder.append(strBounds);
		}
		genAddCode(imports, builder);
		builder.append(genAddEventCode(imports));
		return builder.toString();
	}

	protected void genAddCode(ImportRewrite imports, StringBuilder builder) {
	}

	private String createSetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		ArrayList<IWidgetPropertyDescriptor> properties = adaptable.getPropertyDescriptors();
		for (IWidgetPropertyDescriptor property : properties) {
			if (property.isPropertySet(adaptable.getLnfClassname(), new StructuredSelection(adaptable.getWidget())) && (property.isGencode() || property.isEdited(adaptable))) {
				IPropertyCodeGenerator generator = (IPropertyCodeGenerator) property.getAdapter(IPropertyCodeGenerator.class);
				if (generator != null) {
					String setCode = generator.getJavaCode(adaptable.getWidget(), imports);
					if (setCode != null)
						builder.append(setCode);
				}
			}
		}
		Component widget = adaptable.getWidget();
		if (widget instanceof JComponent) {
			JComponent jcomp = (JComponent) widget;
			JPopupMenu jpm = JavaUtil.getComponentPopupMenu(jcomp);
			if (jpm != null && WidgetAdapter.getWidgetAdapter(jpm) != null) {
				WidgetAdapter jpmAdapter = WidgetAdapter.getWidgetAdapter(jpm);
				IParser parser = (IParser) jpmAdapter.getAdapter(IParser.class);
				if (parser != null) {					
					String getMethodName = parser.getCreationMethodName();
					if (!adaptable.isRoot())
						builder.append(adaptable.getID() + ".");
					builder.append("setComponentPopupMenu(" + getMethodName + "());\n");
				}
			}
		}
		return builder.toString();
	}

	private String genAddEventCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		Set<EventSetDescriptor> keySet = adaptable.getEventDescriptor().keySet();
		if (!keySet.isEmpty()) {
			for (EventSetDescriptor eventSet : keySet) {
				String id = adaptable.getID();
				if (!adaptable.isRoot())
					builder.append(id + ".");
				Method mAdd = eventSet.getAddListenerMethod();
				builder.append(mAdd.getName() + "(");
				IEventListenerModel model = adaptable.getEventDescriptor().get(eventSet);
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
		genAddCode(imports, builder);
		String code = genAddEventCode(imports);
		builder.append(code);
		return builder.toString();
	}

	protected String getNewInstanceCode(ImportRewrite imports) {
		String beanName = imports.addImport(adaptable.getWidgetCodeClassName());
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

	
	public void setAdaptable(IAdaptable adaptable) {
		this.adaptable = (WidgetAdapter) adaptable;
	}

	
	public String getCreationMethodName() {
		return NamespaceUtil.getGetMethodName(adaptable, adaptable.getID());
	}

}
