
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
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.UIResource;

import org.dyno.visual.swing.base.FieldProperty;
import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.borders.action.TitledBorderSwitchAction;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.IAction;

public class TitledBorderAdapter extends BorderAdapter {
	@SuppressWarnings("unchecked")
	@Override
	public Class getBorderClass() {
		return TitledBorder.class;
	}

	@Override
	public String getBorderName() {
		return "TitledBorder";
	}

	@Override
	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		FieldProperty title = new FieldProperty("title", "title",
				TitledBorder.class);
		FieldProperty border = new FieldProperty("border", "border",
				TitledBorder.class);
		FieldProperty titlePosition = new FieldProperty("titlePosition",
				"titlePosition", TitledBorder.class,
				new ItemProviderLabelProviderFactory(new TitlePositionItems()),
				new ItemProviderCellEditorFactory(new TitlePositionItems()));
		FieldProperty titleJustification = new FieldProperty(
				"titleJustification",
				"titleJustification",
				TitledBorder.class,
				new ItemProviderLabelProviderFactory(
						new TitleJustificationItems()),
				new ItemProviderCellEditorFactory(new TitleJustificationItems()));
		FieldProperty titleFont = new FieldProperty("titleFont", "titleFont",
				TitledBorder.class);
		FieldProperty titleColor = new FieldProperty("titleColor",
				"titleColor", TitledBorder.class);
		return new IWidgetPropertyDescriptor[] { title, border, titlePosition,
				titleJustification, titleFont, titleColor };
	}

	@Override
	public IAction getContextAction(JComponent widget) {
		return new TitledBorderSwitchAction(widget);
	}

	@Override
	public Object newInstance(Object bean) {
		return BorderFactory.createTitledBorder("Border Title");
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
		TitledBorder border = (TitledBorder) value;
		Border border2 = border.getBorder();
		String title = border.getTitle();
		Color titleColor = border.getTitleColor();
		Font titleFont = border.getTitleFont();
		int titleJustification = border.getTitleJustification();
		int titlePosition = border.getTitlePosition();
		StringBuilder builder = new StringBuilder();
		builder.append(strBf + ".createTitledBorder(");
		if (border2 instanceof UIResource) {
			builder.append(encodeValue(title, imports));
		} else {
			builder.append(encodeValue(border2, imports));
			builder.append(", ");
			builder.append(encodeValue(title, imports));
			builder.append(", ");
			builder.append(encodeValue(new TitleJustificationItems(),
					titleJustification, imports));
			builder.append(", ");
			builder.append(encodeValue(new TitlePositionItems(), titlePosition,
					imports));
			builder.append(", ");
			builder.append(encodeValue(titleFont, imports));
			builder.append(", ");
			builder.append(encodeValue(titleColor, imports));
		}
		builder.append(")");
		return builder.toString();
	}

}

