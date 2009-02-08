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

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import org.dyno.visual.swing.parser.spi.IParser;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JSplitPaneParser extends CompositeParser {

	@Override
	protected void genAddCode(ImportRewrite imports, StringBuilder builder) {
		JSplitPane jsp = (JSplitPane) adapter.getWidget();
		int oritentation = jsp.getOrientation();
		String fieldName = adapter.getID();
		String prefix = "";
		if (!adapter.isRoot())
			prefix = fieldName + ".";
		if (oritentation == JSplitPane.HORIZONTAL_SPLIT) {
			Component left = jsp.getLeftComponent();
			if (left != null) {
				JComponent leftComponent = (JComponent) left;
				WidgetAdapter leftAdapter = WidgetAdapter
						.getWidgetAdapter(leftComponent);
				if (leftAdapter != null) {
					IParser leftParser = (IParser) leftAdapter
							.getAdapter(IParser.class);
					String leftGetName = leftParser.getCreationMethodName();
					builder.append(prefix + "setLeftComponent(" + leftGetName
							+ "());\n");
				}
			}
			Component right = jsp.getRightComponent();
			if (right != null) {
				JComponent rightComponent = (JComponent) right;
				WidgetAdapter rightAdapter = WidgetAdapter
						.getWidgetAdapter(rightComponent);
				if (rightAdapter != null) {
					IParser rightParser = (IParser) rightAdapter
							.getAdapter(IParser.class);
					String rightGetName = rightParser.getCreationMethodName();
					builder.append(prefix + "setRightComponent(" + rightGetName
							+ "());\n");
				}
			}
		} else {
			Component top = jsp.getTopComponent();
			if (top != null) {
				JComponent topComponent = (JComponent) top;
				WidgetAdapter topAdapter = WidgetAdapter
						.getWidgetAdapter(topComponent);
				if (topAdapter != null) {
					IParser topParser = (IParser) topAdapter
							.getAdapter(IParser.class);
					String topGetName = topParser.getCreationMethodName();
					builder.append(prefix + "setTopComponent(" + topGetName
							+ "());\n");
				}
			}
			Component bottom = jsp.getBottomComponent();
			if (bottom != null) {
				JComponent bottomComponent = (JComponent) bottom;
				WidgetAdapter bottomAdapter = WidgetAdapter
						.getWidgetAdapter(bottomComponent);
				if (bottomAdapter != null) {
					IParser bottomParser = (IParser) bottomAdapter
							.getAdapter(IParser.class);
					String bottomGetName = bottomParser.getCreationMethodName();
					builder.append(prefix + "setBottomComponent("
							+ bottomGetName + "());\n");
				}
			}

		}
	}
}
