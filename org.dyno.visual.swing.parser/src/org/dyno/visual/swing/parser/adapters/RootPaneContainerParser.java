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

import java.awt.Dimension;

import javax.swing.JMenuBar;
import javax.swing.RootPaneContainer;

import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.parser.ParserPlugin;
import org.dyno.visual.swing.parser.spi.IParser;
import org.dyno.visual.swing.plugin.spi.RootPaneContainerAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public abstract class RootPaneContainerParser extends CompositeParser {
	@Override
	protected String createInitCode(ImportRewrite imports) {
		RootPaneContainerAdapter rootPaneContainerAdapter = (RootPaneContainerAdapter) adapter;
		StringBuilder builder = new StringBuilder();
		builder.append(super.createInitCode(imports));
		JPanelParser parser=(JPanelParser) rootPaneContainerAdapter.getContentAdapter().getAdapter(IParser.class);
		if(parser!=null)
			parser.genAddCode(imports, builder);
		if(getJMenuBar()!=null){
			builder.append("setJMenuBar(");
			JMenuBar jmb = getJMenuBar();
			WidgetAdapter jmbAdapter=WidgetAdapter.getWidgetAdapter(jmb);
			String getName=NamespaceManager.getInstance().getGetMethodName(jmbAdapter.getName());
			builder.append(getName+"()");
			builder.append(");\n");
		}
		return builder.toString();
	}
	@Override
	protected void createPostInitCode(StringBuilder builder, ImportRewrite imports) {
		Dimension size = ((RootPaneContainer)adapter.getWidget()).getRootPane().getSize();
		builder.append("setSize(" + size.width + ", "
				+ size.height + ");\n");
	}		
	@Override
	protected boolean createConstructor(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		IMethod cons = type.getMethod(type.getElementName(), new String[0]);
		if (!cons.exists()) {
			StringBuilder builder = new StringBuilder();
			builder.append("public " + type.getElementName() + "(){\n");
			builder.append("initComponent();\n");
			builder.append("}\n");
			try {
				type.createMethod(JavaUtil.formatCode(builder.toString()), null, false, null);
			} catch (JavaModelException e) {
				ParserPlugin.getLogger().error(e);
				return false;
			}
		}
		return true;
	}
	protected abstract JMenuBar getJMenuBar();
}
