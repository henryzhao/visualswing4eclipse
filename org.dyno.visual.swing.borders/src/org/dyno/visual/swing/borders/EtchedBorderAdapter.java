
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
import javax.swing.border.EtchedBorder;

import org.dyno.visual.swing.base.FieldProperty;
import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.borders.action.EtchedBorderSwitchAction;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.IAction;

@SuppressWarnings("unchecked")
public class EtchedBorderAdapter extends BorderAdapter {

    protected int etchType;
    protected Color highlight;
    protected Color shadow;
	
	@Override
	public Class getBorderClass() {
		return EtchedBorder.class;
	}

	
	public String getBorderName() {
		return "EtchedBorder";
	}

	
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		FieldProperty etchTypeProperty = new FieldProperty("etchType", "etchType", EtchedBorder.class, new ItemProviderLabelProviderFactory(new EtchedBorderTypeItems()), new ItemProviderCellEditorFactory(new EtchedBorderTypeItems()));
		FieldProperty highlightProperty = new FieldProperty("highlight", "highlight", EtchedBorder.class);
		FieldProperty shadowProperty = new FieldProperty("shadow", "shadow", EtchedBorder.class);
		return new IWidgetPropertyDescriptor[] {etchTypeProperty, 
				highlightProperty, 
				shadowProperty,
			};
	}

	
	public IAction getContextAction(JComponent widget) {
		return new EtchedBorderSwitchAction(widget);
	}

	
	public Object newInstance(Object bean) {
		return BorderFactory.createEtchedBorder();
	}

	
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	
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

