package org.dyno.visual.swing.parser.adapters;

import java.util.List;

import javax.swing.AbstractButton;

import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.parser.ParserPlugin;
import org.dyno.visual.swing.parser.spi.IParser;
import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class ButtonGroupParser implements IParser, IAdaptableContext {
	@SuppressWarnings("unchecked")
	@Override
	public boolean generateCode(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
		boolean success = true;
		IJavaElement sibling = null;
		if (adapter.getLastName() != null) {
			IField lastField;
			if (!adapter.getLastName().equals(adapter.getName())) {
				lastField = type.getField(getFieldName(adapter.getLastName()));
			} else {
				lastField = type.getField(getFieldName(adapter.getName()));
			}
			if (lastField != null && lastField.exists()) {
				try {
					lastField.delete(true, monitor);
				} catch (Exception e) {
					ParserPlugin.getLogger().error(e);
					success = false;
				}
			}
		}
		IField field = type.getField(getFieldName(adapter.getName()));
		if (field != null && !field.exists()) {
			StringBuilder builder = new StringBuilder();
			builder.append("private");
			builder.append(" ");
			String fqcn = "javax.swing.ButtonGroup";
			String beanName = imports.addImport(fqcn);
			builder.append(beanName);
			builder.append(" ");
			builder.append(getFieldName(adapter.getName()));
			builder.append(";\n");
			try {
				type.createField(builder.toString(), sibling, false, monitor);
			} catch (JavaModelException e) {
				ParserPlugin.getLogger().error(e);
				success = false;
			}
		}
		sibling = null;
		if (adapter.getLastName() != null && !adapter.getLastName().equals(adapter.getName())) {
			String lastGetMethodName = "init"+NamespaceManager.getInstance().getCapitalName(adapter.getLastName());
			IMethod lastMethod = type.getMethod(lastGetMethodName, new String[0]);
			if (lastMethod != null && lastMethod.exists()) {
				try {
					sibling = getSibling(type, lastMethod);
					lastMethod.delete(true, monitor);
				} catch (Exception e) {
					ParserPlugin.getLogger().error(e);
					success = false;
				}
			}
		}
		StringBuilder builder = new StringBuilder();
		String getMethodName = adapter.getCreationMethodName();
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
		builder.append("private void ");
		builder.append(getMethodName);
		builder.append("(){\n");
		String fqcn ="javax.swing.ButtonGroup";
		String beanName = imports.addImport(fqcn);
		builder.append(getFieldName(adapter.getName())+" = new "+beanName+"();\n");
		List buttons = adapter.getElements();
		for(int i=0;i<buttons.size();i++){
			WidgetAdapter btnAdapter = (WidgetAdapter) buttons.get(i);
			AbstractButton button = (AbstractButton) btnAdapter.getWidget();
			WidgetAdapter buttonAdapter = WidgetAdapter.getWidgetAdapter(button);
			builder.append(getFieldName(adapter.getName())+".add("+buttonAdapter.getCreationMethodName()+"());\n");
		}
		builder.append("}\n");
		try {
			if (sibling == null)
				sibling = getInitMethodSibling(type);
			type.createMethod(JavaUtil.formatCode(builder.toString()), sibling,
					false, monitor);
		} catch (JavaModelException e) {
			ParserPlugin.getLogger().error(e);
			success = false;
		}
		adapter.setLastName(adapter.getName());
		return success;		
	}
	private String getFieldName(String name) {
		return NamespaceManager.getInstance().getFieldName(name);
	}
	private IJavaElement getInitMethodSibling(IType type) {
		String initMethodName = "initComponent";
		IMethod method = type.getMethod(initMethodName, new String[0]);
		if (method != null && method.exists()) {
			return getSibling(type, method);
		}
		return null;
	}	
	private IJavaElement getSibling(IType type, IJavaElement element) {
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

	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adapter = (InvisibleAdapter) adaptable;
	}
	private InvisibleAdapter adapter;
}
