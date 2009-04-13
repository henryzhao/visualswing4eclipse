
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

package org.dyno.visual.swing.borders;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.LineBorder;

import org.dyno.visual.swing.base.FieldProperty;
import org.dyno.visual.swing.borders.action.LineBorderSwitchAction;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.IAction;


@SuppressWarnings("unchecked")
public class LineBorderAdapter extends BorderAdapter{
	
	public IAction getContextAction(JComponent widget) {
		return new LineBorderSwitchAction(widget);
	}

	
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		FieldProperty thicknessProperty=new FieldProperty("thickness", "thickness", LineBorder.class);
		FieldProperty lineColorProperty=new FieldProperty("lineColor", "lineColor", LineBorder.class);
		FieldProperty roundedCornersProperty = new FieldProperty("roundedCorners","roundedCorners", LineBorder.class);		
		return new IWidgetPropertyDescriptor[]{thicknessProperty, lineColorProperty, roundedCornersProperty};
	}

	
	public Class getBorderClass() {
		return LineBorder.class;
	}

	
	public String getBorderName() {
		return "LineBorder";
	}

	
	public Object newInstance(Object bean) {
		return BorderFactory.createLineBorder(Color.black);
	}

	
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	
	public String getJavaCode(Object value, ImportRewrite imports) {
		if(value==null)
			return "null";
		String strBf=imports.addImport("javax.swing.border.LineBorder");
		LineBorder border = (LineBorder)value;
		int thickness = border.getThickness();		
		Color lineColor = border.getLineColor();
		boolean roundedCorners = border.getRoundedCorners();
		StringBuilder builder = new StringBuilder();
		builder.append("new "+strBf+"(");
		builder.append(encodeValue(lineColor, imports));
		builder.append(", ");
		builder.append(encodeValue(thickness, imports));
		builder.append(", ");
		builder.append(encodeValue(roundedCorners, imports));
		builder.append(")");
		return builder.toString();
	}
}

