package org.dyno.visual.swing.types.editor;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class FloatCellEditorValidator implements ICellEditorValidator {

	@Override
	public String isValid(Object value) {
		String string = (String) value;
		try {
			Float.parseFloat(string);
		} catch (NumberFormatException nfe) {
			return "Incorrect float format:{0}";
		}
		return null;
	}
}
