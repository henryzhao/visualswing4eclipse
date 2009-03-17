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

import javax.swing.Icon;
import javax.swing.JTabbedPane;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.TypeAdapter;
import org.dyno.visual.swing.parser.spi.IParser;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JTabbedPaneParser extends CompositeParser {

	@Override
	protected void genAddCode(ImportRewrite imports, StringBuilder builder) {
		JTabbedPane jtp = (JTabbedPane) adaptable.getWidget();
		CompositeAdapter ca = (CompositeAdapter) adaptable;
		int count = ca.getChildCount();
		for (int i = 0; i < count; i++) {
			Component child = ca.getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			IParser childParser = (IParser) childAdapter.getAdapter(IParser.class);
			String getMethodName = childParser.getCreationMethodName();
			if (!adaptable.isRoot())
				builder.append(ca.getID() + ".");
			builder.append("addTab(");
			String title = jtp.getTitleAt(i);
			String tip = jtp.getToolTipTextAt(i);
			Icon icon = jtp.getIconAt(i);
			builder.append("\"" + title + "\", ");
			if (icon == null && tip == null) {
				builder.append(getMethodName + "());\n");
			} else {
				TypeAdapter ta = ExtensionRegistry.getTypeAdapter(Icon.class);
				if (ta != null && ta.getCodegen() != null) {
					String init = ta.getCodegen()
							.getInitJavaCode(icon, imports);
					if (init != null)
						builder.append(init);
					builder.append(ta.getCodegen().getJavaCode(icon, imports));
					builder.append(", ");
				}
				if (tip == null) {
					builder.append(getMethodName + "());\n");
				} else {
					builder.append(getMethodName + "(), ");
					builder.append("\"" + tip + "\");\n");
				}
			}
		}
	}
}
