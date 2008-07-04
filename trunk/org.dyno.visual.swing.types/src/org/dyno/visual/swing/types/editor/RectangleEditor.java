/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor;

import java.awt.Rectangle;
import java.util.StringTokenizer;

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.dyno.visual.swing.types.endec.RectangleWrapper;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class RectangleEditor extends RectangleWrapper implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		CellEditor editor = new TextCellEditor(parent);
		editor.setValidator(new RectangleCellEditorValidator());
		return editor;
	}

	@Override
	public Object decodeValue(Object value) {
		if (value == null)
			return null;
		else if (value.equals("null")) {
			return null;
		} else {
			String sValue = value.toString().trim();
			sValue = sValue.substring(1, sValue.length() - 1);
			StringTokenizer tokenizer = new StringTokenizer(sValue, ",");
			String sX = tokenizer.nextToken().trim();
			String sY = tokenizer.nextToken().trim();
			String sWidth = tokenizer.nextToken().trim();
			String sHeight = tokenizer.nextToken().trim();
			int x = 0;
			int y = 0;
			int width = 0;
			int height = 0;
			try {
				x = Integer.parseInt(sX);
				y = Integer.parseInt(sY);
				width = Integer.parseInt(sWidth);
				height = Integer.parseInt(sHeight);
			} catch (NumberFormatException nfe) {
			}
			return new Rectangle(x, y, width, height);
		}
	}

	@Override
	public Object encodeValue(Object value) {
		if (value == null)
			return "null";
		else {
			Rectangle bounds = (Rectangle) value;
			return "(" + bounds.x + ", " + bounds.y + ", " + bounds.width + ", " + bounds.height + ")";
		}
	}
}
