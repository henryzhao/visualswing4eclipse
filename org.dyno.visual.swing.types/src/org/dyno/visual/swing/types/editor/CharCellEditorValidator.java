
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

package org.dyno.visual.swing.types.editor;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class CharCellEditorValidator implements ICellEditorValidator {

	
	public String isValid(Object value) {
		if(value==null)
			return Messages.CharCellEditorValidator_Incorrect_Format;
		String string = ((String) value).trim();
		if(string.length()==0)
			return Messages.CharCellEditorValidator_Incorrect_Format;
		if(string.length()>2)
			return Messages.CharCellEditorValidator_Incorrect_Format_1;
		if(string.length()==2&&!string.equals("\\0")) //$NON-NLS-1$
			return Messages.CharCellEditorValidator_Incorrect_Format_1;
		else
			return null;
	}
}

