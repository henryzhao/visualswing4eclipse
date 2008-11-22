/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor;

import java.awt.Insets;
import java.util.StringTokenizer;

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.dyno.visual.swing.types.endec.InsetsWrapper;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class InsetsEditor extends InsetsWrapper implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		CellEditor editor = new TextCellEditor(parent);
		editor.setValidator(new InsetsCellEditorValidator());
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
			String sTop = tokenizer.nextToken().trim();
			String sLeft = tokenizer.nextToken().trim();
			String sBottom = tokenizer.nextToken().trim();
			String sRight = tokenizer.nextToken().trim();
			int top = 0;
			int left = 0;
			int bottom = 0;
			int right = 0;
			try {
				top = Integer.parseInt(sTop);
				left = Integer.parseInt(sLeft);
				bottom = Integer.parseInt(sBottom);
				right = Integer.parseInt(sRight);
			} catch (NumberFormatException nfe) {
			}
			return new Insets(top, left, bottom, right);
		}
	}

	@Override
	public Object encodeValue(Object value) {
		if (value == null)
			return "null";
		else {
			Insets insets = (Insets) value;
			return "(" + insets.top + ", " + insets.left + ", " + insets.bottom + ", " + insets.right + ")";
		}
	}
}
