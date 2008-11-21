package org.dyno.visual.swing.widgets;

import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.swt.graphics.Image;

public class ButtonGroupAdapter implements IAdapter {
	private static final String BUTTON_GROUP_ICON = "/icons/button_group_16.png";
	private static int VAR_INDEX=0;
	private static Image iconImage;
	static{
		iconImage = WidgetPlugin.getSharedImage(BUTTON_GROUP_ICON);
	}
	private String name;
	private String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setName(String name) {
		this.name = name;
	}
	private ButtonGroup group;
	public ButtonGroupAdapter(){
		name = "buttonGroup"+(VAR_INDEX++);
		group = new ButtonGroup();
		
	}
	public ButtonGroup getButtonGroup(){
		return group;
	}
	@Override
	public Image getIconImage() {
		return iconImage;
	}

	@Override
	public String getName() {
		return name;
	}
	@Override
	public boolean generateCode(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
		boolean success = true;
		IJavaElement sibling = null;
		if (getLastName() != null) {
			IField lastField;
			if (!getLastName().equals(getName())) {
				lastField = type.getField(getFieldName(getLastName()));
			} else {
				lastField = type.getField(getFieldName(getName()));
			}
			if (lastField != null && lastField.exists()) {
				try {
					lastField.delete(true, monitor);
				} catch (Exception e) {
					success = false;
				}
			}
		}
		IField field = type.getField(getFieldName(getName()));
		if (field != null && !field.exists()) {
			StringBuilder builder = new StringBuilder();
			builder.append("private");
			builder.append(" ");
			String fqcn = "javax.swing.ButtonGroup";
			String beanName = imports.addImport(fqcn);
			builder.append(beanName);
			builder.append(" ");
			builder.append(getFieldName(getName()));
			builder.append(";\n");
			try {
				type.createField(builder.toString(), sibling, false, monitor);
			} catch (JavaModelException e) {
				success = false;
			}
		}
		sibling = null;
		if (getLastName() != null && !getLastName().equals(getName())) {
			String lastGetMethodName = NamespaceManager.getInstance().getGetMethodName(getLastName());
			IMethod lastMethod = type.getMethod(lastGetMethodName,
					new String[0]);
			if (lastMethod != null && lastMethod.exists()) {
				try {
					sibling = getSibling(type, lastMethod);
					lastMethod.delete(true, monitor);
				} catch (Exception e) {
					success = false;
				}
			}
		}
		StringBuilder builder = new StringBuilder();
		String getMethodName = getCreationMethodName();
		IMethod method = type.getMethod(getMethodName, new String[0]);
		if (method != null && method.exists()) {
			try {
				sibling = getSibling(type, method);
				method.delete(false, monitor);
			} catch (JavaModelException e) {
				success = false;
			}
		}
		builder.append("private void ");
		builder.append(getMethodName);
		builder.append("(){\n");
		String fqcn ="javax.swing.ButtonGroup";
		String beanName = imports.addImport(fqcn);
		builder.append(getFieldName(getName())+" = new "+beanName+"();\n");
		Enumeration<AbstractButton> buttons = group.getElements();
		while(buttons.hasMoreElements()){
			AbstractButton button = buttons.nextElement();
			WidgetAdapter buttonAdapter = WidgetAdapter.getWidgetAdapter(button);
			builder.append(getFieldName(getName())+".add("+buttonAdapter.getCreationMethodName()+"());\n");
		}
		builder.append("}\n");
		try {
			if (sibling == null)
				sibling = getInitMethodSibling(type);
			type.createMethod(JavaUtil.formatCode(builder.toString()), sibling,
					false, monitor);
		} catch (JavaModelException e) {
			success = false;
		}
		setLastName(getName());
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
			e.printStackTrace();
		}
		return null;
	}	
	@Override
	public String getCreationMethodName() {
		return "init"+NamespaceManager.getInstance().getCapitalName(name);
	}
}
