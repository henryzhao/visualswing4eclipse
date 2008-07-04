/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor;

import java.awt.Color;

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.dyno.visual.swing.types.base.ColorWrapper;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;

public class ColorEditor extends ColorWrapper implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		return new ColorCellEditor(parent);
	}

	@Override
	public Object decodeValue(Object value) {
		if (value == null)
			return null;
		RGB rgb = (RGB) value;
		return new Color(rgb.red, rgb.green, rgb.blue);
	}

	@Override
	public Object encodeValue(Object value) {
		if (value == null)
			return null;
		Color color = (Color) value;
		return new RGB(color.getRed(), color.getGreen(), color.getBlue());
	}
}
