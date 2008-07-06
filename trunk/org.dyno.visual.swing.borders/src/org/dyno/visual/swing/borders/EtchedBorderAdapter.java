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
import javax.swing.border.EtchedBorder;

import org.dyno.visual.swing.base.FieldProperty;
import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.IAction;

public class EtchedBorderAdapter extends BorderAdapter {

    protected int etchType;
    protected Color highlight;
    protected Color shadow;
	
	@SuppressWarnings("unchecked")
	@Override
	public Class getBorderClass() {
		return EtchedBorder.class;
	}

	@Override
	public String getBorderName() {
		return "EtchedBorder";
	}

	@Override
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		FieldProperty etchTypeProperty = new FieldProperty("etchType", "etchType", EtchedBorder.class, new ItemProviderLabelProviderFactory(new EtchedBorderTypeItems()), new ItemProviderCellEditorFactory(new EtchedBorderTypeItems()));
		FieldProperty highlightProperty = new FieldProperty("highlight", "highlight", EtchedBorder.class);
		FieldProperty shadowProperty = new FieldProperty("shadow", "shadow", EtchedBorder.class);
		return new IWidgetPropertyDescriptor[] {etchTypeProperty, 
				highlightProperty, 
				shadowProperty,
			};
	}

	@Override
	public IAction getContextAction(JComponent widget) {
		return new EtchedBorderSwitchAction(widget);
	}

	@Override
	public Object newInstance(Object bean) {
		return BorderFactory.createEtchedBorder();
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
		EtchedBorder border = (EtchedBorder)value;
		int type = border.getEtchType();
		Color highlightColor = border.getHighlightColor();
		Color shadowColor = border.getShadowColor();
		StringBuilder builder = new StringBuilder();
		builder.append(strBf+".createEtchedBorder(");
		builder.append(encodeValue(new EtchedBorderTypeItems(), type, imports));
		builder.append(", ");
		builder.append(encodeValue(highlightColor, imports));
		builder.append(", ");		
		builder.append(encodeValue(shadowColor, imports));
		builder.append(")");
		return builder.toString();
	}

}
