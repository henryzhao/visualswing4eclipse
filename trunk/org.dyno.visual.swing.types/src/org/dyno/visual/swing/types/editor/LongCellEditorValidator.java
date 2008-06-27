package org.dyno.visual.swing.types.editor;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class LongCellEditorValidator implements ICellEditorValidator {

	@Override
	public String isValid(Object value) {
		String string = (String) value;
		try {
			Integer.parseInt(string);
		} catch (NumberFormatException nfe) {
			return string + " should be a long, the format is incorrect!";
		}
		return null;
	}
}
