
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

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import org.dyno.visual.swing.base.FieldProperty;
import org.dyno.visual.swing.borders.action.CompoundBorderSwitchAction;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
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
@SuppressWarnings("unchecked")
public class CompoundBorderAdapter extends BorderAdapter {

	
	public Class getBorderClass() {
		return CompoundBorder.class;
	}

	
	public String getBorderName() {
		return "CompoundBorder";
	}

	
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		FieldProperty outside = new FieldProperty("outsideBorder", "outsideBorder", CompoundBorder.class);
		FieldProperty inside = new FieldProperty("insideBorder", "insideBorder", CompoundBorder.class);
		return new IWidgetPropertyDescriptor[] {outside, inside};
	}

	
	public IAction getContextAction(JComponent widget) {
		return new CompoundBorderSwitchAction(widget);
	}

	
	public Object newInstance(Object bean) {
		return BorderFactory.createCompoundBorder();
	}

	
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	
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

