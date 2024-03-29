
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

public class FloatCellEditorValidator implements ICellEditorValidator {

	
	public String isValid(Object value) {
		String string = (String) value;
		try {
			Float.parseFloat(string);
		} catch (NumberFormatException nfe) {
			return Messages.FloatCellEditorValidator_Incorrect_Format;
		}
		return null;
	}
}

