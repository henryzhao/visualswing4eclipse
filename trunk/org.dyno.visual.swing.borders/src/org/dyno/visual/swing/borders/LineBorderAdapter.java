/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.borders;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.LineBorder;

import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.FieldProperty;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.IAction;


public class LineBorderAdapter extends BorderAdapter{
	@Override
	public IAction getContextAction(JComponent widget) {
		return new LineBorderSwitchAction(widget);
	}

	@Override
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		FieldProperty thicknessProperty=new FieldProperty("thickness", "thickness", LineBorder.class);
		FieldProperty lineColorProperty=new FieldProperty("lineColor", "lineColor", LineBorder.class);
		FieldProperty roundedCornersProperty = new FieldProperty("roundedCorners","roundedCorners", LineBorder.class);		
		return new IWidgetPropertyDescriptor[]{thicknessProperty, lineColorProperty, roundedCornersProperty};
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class getBorderClass() {
		return LineBorder.class;
	}

	@Override
	public String getBorderName() {
		return "LineBorder";
	}

	@Override
	public Object newInstance(Object bean) {
		return BorderFactory.createLineBorder(Color.black);
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	@Override
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
