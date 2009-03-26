
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
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.UIResource;

import org.dyno.visual.swing.base.FieldProperty;
import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.borders.action.TitledBorderSwitchAction;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.IAction;

@SuppressWarnings("unchecked")
public class TitledBorderAdapter extends BorderAdapter {
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
	private IEditor editor;
	@Override
	public IEditor getEditorAt(JComponent owner, Point hotspot) {
		Insets insets = owner.getInsets();
		int x = hotspot.x;
		int y = hotspot.y;
		int w = owner.getWidth();
		int h = owner.getHeight();
		if (x >= 0
				&& x < w
				&& y >= 0
				&& y < h
				&& (x < insets.left || x >= w - insets.right || y < insets.top || y >= h
						- insets.bottom)) {
			if (editor == null)
				editor = new LabelEditor();
			return editor;
		}
		return null;
	}

	@Override
	public Rectangle getEditorBounds(JComponent owner, Point hotspot) {
		Insets insets = owner.getInsets();
		int x = hotspot.x;
		int y = hotspot.y;
		int w = owner.getWidth();
		int h = owner.getHeight();
		if (x >= 0
				&& x < w
				&& y >= 0
				&& y < h
				&& (x < insets.left || x >= w - insets.right || y < insets.top || y >= h
						- insets.bottom)) {
			Font f = owner.getFont();
			FontMetrics fm = owner.getFontMetrics(f);
			TitledBorder titledBorder = (TitledBorder) owner.getBorder();
			String title = titledBorder.getTitle();
			int width = fm.stringWidth(title);
			return new Rectangle(insets.left, 0, width + insets.left+insets.right, 22);
		}
		return null;
	}

	@Override
	public Object getWidgetValue(JComponent owner, Point hotspot) {
		TitledBorder titledBorder = (TitledBorder) owner.getBorder();
		return titledBorder.getTitle();
	}

	@Override
	public void setWidgetValue(JComponent owner, Object value, Point hotspot) {
		TitledBorder titledBorder = (TitledBorder) owner.getBorder();
		titledBorder.setTitle((String)value);
	}

}

