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
import java.awt.LayoutManager;

import javax.swing.JPanel;

import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.parser.ParserPlugin;
import org.dyno.visual.swing.parser.spi.ILayoutParser;
import org.dyno.visual.swing.parser.spi.IParser;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IConstants;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JPanelParser extends CompositeParser implements IParser, IConstants {

	protected void genAddCode(ImportRewrite imports, StringBuilder builder) {
		JPanel panel = (JPanel) adapter.getWidget();
		LayoutManager layout = panel.getLayout();
		if (layout == null) {
			if (!adapter.isRoot())
				builder.append(getFieldName(adapter.getName()) + ".");
			builder.append("setLayout(null);\n");
			int count = ((CompositeAdapter) adapter).getChildCount();
			for (int i = 0; i < count; i++) {
				Component child = ((CompositeAdapter) adapter).getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter
						.getWidgetAdapter(child);
				String getMethodName = childAdapter.getCreationMethodName();
				if (!adapter.isRoot())
					builder.append(getFieldName(adapter.getName()) + ".");
				builder.append("add(" + getMethodName + "());\n");
			}
		} else {
			LayoutAdapter layoutAdapter = ((CompositeAdapter) adapter)
					.getLayoutAdapter();
			if (layoutAdapter != null) {
				ILayoutParser parser = (ILayoutParser) layoutAdapter
						.getAdapter(ILayoutParser.class);
				if (parser != null)
					builder.append(parser.createCode(imports));
			}
		}
	}
	@Override
	protected boolean createConstructor(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
		IMethod cons = type.getMethod(type.getElementName(), new String[0]);
		if (!cons.exists()) {
			StringBuilder builder = new StringBuilder();
			builder.append("public " + type.getElementName() + "(){\n");
			builder.append(INIT_METHOD_NAME+"();\n");
			builder.append("}\n");
			try {
				type.createMethod(JavaUtil.formatCode(builder.toString()),
						null, false, null);
			} catch (JavaModelException e) {
				ParserPlugin.getLogger().error(e);
				return false;
			}
		}
		return true;
	}
}
