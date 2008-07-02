package org.dyno.visual.swing.types.editor;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class CharCellEditorValidator implements ICellEditorValidator {

	@Override
	public String isValid(Object value) {
		if(value==null)
			return "Incorrect character format!";
		String string = ((String) value).trim();
		if(string.length()==0)
			return "Incorrect character format!";
		if(string.length()>2)
			return "Incorrect character format:{0}";
		if(string.length()==2&&!string.equals("\\0"))
			return "Incorrect character format:{0}";
		else
			return null;
	}
}
