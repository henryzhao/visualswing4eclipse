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

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JSplitPaneParser extends CompositeParser {

	@Override
	protected String createGetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.createGetCode(imports));
		JSplitPane jsp = (JSplitPane) adapter.getWidget();
		int oritentation = jsp.getOrientation();
		String fieldName = getFieldName(adapter.getName());
		if (oritentation == JSplitPane.HORIZONTAL_SPLIT) {
			Component left = jsp.getLeftComponent();
			if (left != null) {
				JComponent leftComponent = (JComponent) left;
				WidgetAdapter leftAdapter = WidgetAdapter
						.getWidgetAdapter(leftComponent);
				if (leftAdapter != null) {
					String leftGetName = leftAdapter.getCreationMethodName();
					builder.append(fieldName + ".setLeftComponent("
							+ leftGetName + "());\n");
				}
			}
			Component right = jsp.getRightComponent();
			if (right != null) {
				JComponent rightComponent = (JComponent) right;
				WidgetAdapter rightAdapter = WidgetAdapter
						.getWidgetAdapter(rightComponent);
				if (rightAdapter != null) {
					String rightGetName = rightAdapter.getCreationMethodName();
					builder.append(fieldName + ".setRightComponent("
							+ rightGetName + "());\n");
				}
			}
		} else {
			Component top = jsp.getTopComponent();
			if (top != null) {
				JComponent topComponent = (JComponent) top;
				WidgetAdapter topAdapter = WidgetAdapter
						.getWidgetAdapter(topComponent);
				if (topAdapter != null) {
					String topGetName = topAdapter.getCreationMethodName();
					builder.append(fieldName + ".setTopComponent(" + topGetName
							+ "());\n");
				}
			}
			Component bottom = jsp.getBottomComponent();
			if (bottom != null) {
				JComponent bottomComponent = (JComponent) bottom;
				WidgetAdapter bottomAdapter = WidgetAdapter
						.getWidgetAdapter(bottomComponent);
				if (bottomAdapter != null) {
					String bottomGetName = bottomAdapter.getCreationMethodName();
					builder.append(fieldName + ".setBottomComponent("
							+ bottomGetName + "());\n");
				}
			}

		}
		return builder.toString();
	}
}
