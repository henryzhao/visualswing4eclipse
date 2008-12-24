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

import javax.swing.BoxLayout;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class BoxLayoutParser extends LayoutParser {

	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		BoxLayout layout = (BoxLayout) layoutAdapter.getContainer().getLayout();
		int axis = layout.getAxis();
		String name=imports.addImport("javax.swing.BoxLayout");
		String strAxis = "X_AXIS";
		switch (axis) {
		case BoxLayout.X_AXIS:
			strAxis = "X_AXIS";
			break;
		case BoxLayout.Y_AXIS:
			strAxis = "Y_AXIS";
			break;
		case BoxLayout.LINE_AXIS:
			strAxis = "LINE_AXIS";
			break;
		case BoxLayout.PAGE_AXIS:
			strAxis = "PAGE_AXIS";
			break;
		}
		strAxis = name+"."+strAxis;
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(layoutAdapter.getContainer());	
		String conName=adapter.isRoot()?"this":getFieldName(adapter.getName());
		return "new "+name+"("+conName+", "+strAxis+")";
	}

}
