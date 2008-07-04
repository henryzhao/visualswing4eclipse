/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.borders;

import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.IAction;
/**
 * 
 * EmptyBorderAdapter
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class EmptyBorderAdapter extends BorderAdapter {

	@SuppressWarnings("unchecked")
	@Override
	public Class getBorderClass() {
		return EmptyBorder.class;
	}

	@Override
	public String getBorderName() {
		return "EmptyBorder";
	}

	@Override
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		InsetsProperty insetsProperty = new InsetsProperty() {
			@SuppressWarnings("unchecked")
			@Override
			protected Class getBorderClass() {
				return EmptyBorder.class;
			}
		};
		return new IWidgetPropertyDescriptor[] { insetsProperty };
	}

	@Override
	public IAction getContextAction(JComponent widget) {
		return new EmptyBorderSwitchAction(widget);
	}

	@Override
	public Object newInstance(Object bean) {
		return BorderFactory.createEmptyBorder();
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
		EmptyBorder border = (EmptyBorder)value;
		Insets insets = border.getBorderInsets();
		StringBuilder builder = new StringBuilder();
		builder.append(strBf+".createEmptyBorder(");
		builder.append(insets.top);
		builder.append(", ");
		builder.append(insets.left);
		builder.append(", ");
		builder.append(insets.bottom);
		builder.append(", ");
		builder.append(insets.right);
		builder.append(")");
		return builder.toString();
	}

}
