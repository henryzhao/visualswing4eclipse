package org.dyno.visual.swing.types.editor;

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class FontEditor implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		return new FontCellEditor(parent);
	}

	@Override
	public Object decodeValue(Object value) {
		if (value == null)
			return null;
		org.eclipse.swt.graphics.FontData data = (org.eclipse.swt.graphics.FontData) value;
		int s = data.getStyle();
		int h = data.getHeight();
		int style = java.awt.Font.PLAIN;
		if ((s & SWT.BOLD) != 0) {
			style |= java.awt.Font.BOLD;
		}
		if ((s & SWT.ITALIC) != 0) {
			style |= java.awt.Font.ITALIC;
		}
		return new java.awt.Font(data.getName(), style, h);
	}

	@Override
	public Object encodeValue(Object value) {
		if (value == null)
			return null;
		java.awt.Font awtFont = (java.awt.Font) value;
		int style = 0;
		int awtStyle = awtFont.getStyle();
		if ((awtStyle & java.awt.Font.BOLD) != 0)
			style |= SWT.BOLD;
		if ((awtStyle & java.awt.Font.ITALIC) != 0)
			style |= SWT.ITALIC;
		return new org.eclipse.swt.graphics.FontData(awtFont.getFamily(), awtFont.getSize(), style);
	}
}
