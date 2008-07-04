/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.borders;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.FieldProperty;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.IAction;
/**
 * 
 * CompoundBorderAdapter
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class CompoundBorderAdapter extends BorderAdapter {

	@SuppressWarnings("unchecked")
	@Override
	public Class getBorderClass() {
		return CompoundBorder.class;
	}

	@Override
	public String getBorderName() {
		return "CompoundBorder";
	}

	@Override
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		FieldProperty outside = new FieldProperty("outsideBorder", "outsideBorder", CompoundBorder.class);
		FieldProperty inside = new FieldProperty("insideBorder", "insideBorder", CompoundBorder.class);
		return new IWidgetPropertyDescriptor[] {outside, inside};
	}

	@Override
	public IAction getContextAction(JComponent widget) {
		return new CompoundBorderSwitchAction(widget);
	}

	@Override
	public Object newInstance(Object bean) {
		return BorderFactory.createCompoundBorder();
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if(value==null)
			return "null";
		String strBf=imports.addImport("javax.swing.BorderFactory");
		CompoundBorder border = (CompoundBorder)value;
		Border inside = border.getInsideBorder();
		Border outside = border.getOutsideBorder();
		StringBuilder builder = new StringBuilder();
		builder.append(strBf+".createCompoundBorder(");
		builder.append(encodeValue(outside, imports));
		builder.append(", ");
		builder.append(encodeValue(inside, imports));
		builder.append(")");
		return builder.toString();
	}
}
