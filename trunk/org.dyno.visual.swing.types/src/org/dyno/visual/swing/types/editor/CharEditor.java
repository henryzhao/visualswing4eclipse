package org.dyno.visual.swing.types.editor;

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class CharEditor implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		CellEditor editor = new TextCellEditor(parent);
		editor.setValidator(new CharCellEditorValidator());
		return editor;
	}

	@Override
	public Object decodeValue(Object value) {
		if (value == null)
			return '\0';
		else if (value.equals("\\0")) {
			return '\0';
		} else {
			String sValue = value.toString().trim();
			return sValue.charAt(0);
		}
	}

	@Override
	public Object encodeValue(Object value) {
		if (value == null) {
			return "\\0";
		} else if (value instanceof Character) {
			Character character = (Character) value;
			if (character.charValue() == '\0')
				return "\\0";
			else
				return character.toString();
		}
		return value.toString();
	}
}
