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

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import org.dyno.visual.swing.parser.spi.IParser;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JFrameParser extends RootPaneContainerParser {
	@Override
	protected JMenuBar getJMenuBar() {
		JFrame jframe = (JFrame) adaptable.getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		return jmb;
	}

	@Override
	protected void genAddCode(ImportRewrite imports, StringBuilder builder) {
		JFrame me = (JFrame) adaptable.getWidget();
		Component cPane = me.getContentPane();
		if (WidgetAdapter.getWidgetAdapter(cPane) != null && WidgetAdapter.getWidgetAdapter(cPane).getID() != null) {
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(cPane);
			IParser childParser = (IParser) childAdapter.getAdapter(IParser.class);
			String getMethodName = childParser.getCreationMethodName();
			builder.append("setContentPane(" + getMethodName + "());\n");
		} else {
			super.genAddCode(imports, builder);
		}
	}
}
