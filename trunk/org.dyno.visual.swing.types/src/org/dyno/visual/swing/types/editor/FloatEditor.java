package org.dyno.visual.swing.types.editor;

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.dyno.visual.swing.types.endec.FloatWrapper;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class FloatEditor extends FloatWrapper implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		CellEditor editor = new TextCellEditor(parent);
		editor.setValidator(new FloatCellEditorValidator());
		return editor;
	}

	@Override
	public Object decodeValue(Object value) {
		if (value == null)
			return 0.0f;
		else {
			String sValue = value.toString().trim();
			return Float.valueOf(sValue);
		}
	}

	@Override
	public Object encodeValue(Object value) {
		if (value == null)
			return "0.0";
		else
			return value.toString();
	}
}
