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

import java.util.List;

import javax.swing.AbstractButton;

import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.parser.NamespaceUtil;
import org.dyno.visual.swing.parser.ParserPlugin;
import org.dyno.visual.swing.parser.spi.IParser;
import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.IConstants;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelStatus;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.ui.refactoring.RenameSupport;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

@SuppressWarnings("unchecked")
public class ButtonGroupParser implements IParser, IAdaptableContext,IConstants {
	@Override
	public boolean renameField(IType type, IProgressMonitor monitor) {
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
					IWorkbenchWindow window=JavaUtil.getEclipseWindow();
					Shell parent = JavaUtil.getEclipseShell();					
					rs.perform(parent, window);
					adapter.setLastName(name);
					return true;
				}
			} catch (JavaModelException jme){
				IJavaModelStatus status=jme.getJavaModelStatus();
				if(!status.isDoesNotExist()){
					ParserPlugin.getLogger().error(jme);
				}else{
					return true;
				}
			} catch (Exception e) {
				ParserPlugin.getLogger().error(e);
			}
			return false;
		} else
			return true;
	}
	@Override
	public boolean generateCode(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
		boolean success = true;
		String id = adapter.getID();
		IField field = type.getField(getFieldName(id));
		IJavaElement sibling = null;
		if (field != null && !field.exists()) {
			StringBuilder builder = new StringBuilder();
			builder.append("private");
			builder.append(" ");
			String fqcn = "javax.swing.ButtonGroup";
			String beanName = imports.addImport(fqcn);
			builder.append(beanName);
			builder.append(" ");
			builder.append(getFieldName(id));
			builder.append(";\n");
			try {
				type.createField(builder.toString(), sibling, false, monitor);
			} catch (JavaModelException e) {
				ParserPlugin.getLogger().error(e);
				success = false;
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
				ParserPlugin.getLogger().error(e);
				success = false;
			}
		}
		builder.append("private void ");
		builder.append(getMethodName);
		builder.append("(){\n");
		String fqcn ="javax.swing.ButtonGroup";
		String beanName = imports.addImport(fqcn);
		builder.append(getFieldName(id)+" = new "+beanName+"();\n");
		List buttons = adapter.getElements();
		for(int i=0;i<buttons.size();i++){
			WidgetAdapter btnAdapter = (WidgetAdapter) buttons.get(i);
			AbstractButton button = (AbstractButton) btnAdapter.getWidget();
			WidgetAdapter buttonAdapter = WidgetAdapter.getWidgetAdapter(button);
			IParser btnParser = (IParser) buttonAdapter.getAdapter(IParser.class);
			builder.append(getFieldName(id)+".add("+btnParser.getCreationMethodName()+"());\n");
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
		return success;		
	}
	@Override
	public String getCreationMethodName() {
		return "init"+NamespaceUtil.getCapitalName(adapter.getID());
	}	
	private String getFieldName(String name) {
		return NamespaceUtil.getFieldName(name);
	}
	private IJavaElement getInitMethodSibling(IType type) {
		IMethod method = type.getMethod(INIT_METHOD_NAME, new String[0]);
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
