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
import javax.swing.border.BevelBorder;

import org.dyno.visual.swing.base.FieldProperty;
import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.borders.action.BevelBorderSwitchAction;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.IAction;

/**
 * 
 * BevelBorderAdapter
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class BevelBorderAdapter extends BorderAdapter {
	@SuppressWarnings("unchecked")
	@Override
	public Class getBorderClass() {
		return BevelBorder.class;
	}

	@Override
	public String getBorderName() {
		return "BevelBorder";
	}

	@Override
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		FieldProperty bevelTypeProperty = new FieldProperty("bevelType", "bevelType", BevelBorder.class, new ItemProviderLabelProviderFactory(
				new BevelBorderTypeItems()), new ItemProviderCellEditorFactory(new BevelBorderTypeItems()));
		FieldProperty highlightOuterProperty = new FieldProperty("highlightOuter", "highlightOuter", BevelBorder.class);
		FieldProperty highlightInnerProperty = new FieldProperty("highlightInner", "highlightInner", BevelBorder.class);
		FieldProperty shadowInnerProperty = new FieldProperty("shadowInner", "shadowInner", BevelBorder.class);
		FieldProperty shadowOuterProperty = new FieldProperty("shadowOuter", "shadowOuter", BevelBorder.class);
		return new IWidgetPropertyDescriptor[] { bevelTypeProperty, highlightOuterProperty, highlightInnerProperty, shadowInnerProperty, shadowOuterProperty };
	}

	@Override
	public IAction getContextAction(JComponent widget) {
		return new BevelBorderSwitchAction((JComponent) widget);
	}

	@Override
	public Object newInstance(Object bean) {
		return BorderFactory.createBevelBorder(BevelBorder.LOWERED);
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return "null";
		String strBf = imports.addImport("javax.swing.BorderFactory");
		BevelBorder bevelBorder = (BevelBorder) value;
		int type = bevelBorder.getBevelType();
		Color highlightInnerColor = bevelBorder.getHighlightInnerColor();
		Color highlightOuterColor = bevelBorder.getHighlightOuterColor();
		Color shadowInnerColor = bevelBorder.getShadowInnerColor();
		Color shadowOuterColor = bevelBorder.getShadowOuterColor();
		StringBuilder builder = new StringBuilder();
		builder.append(strBf + ".createBevelBorder(");
		builder.append(encodeValue(new BevelBorderTypeItems(), type, imports));
		builder.append(", ");
		builder.append(encodeValue(highlightOuterColor, imports));
		builder.append(", ");
		builder.append(encodeValue(highlightInnerColor, imports));
		builder.append(", ");
		builder.append(encodeValue(shadowOuterColor, imports));
		builder.append(", ");
		builder.append(encodeValue(shadowInnerColor, imports));
		builder.append(")");
		return builder.toString();
	}

}

