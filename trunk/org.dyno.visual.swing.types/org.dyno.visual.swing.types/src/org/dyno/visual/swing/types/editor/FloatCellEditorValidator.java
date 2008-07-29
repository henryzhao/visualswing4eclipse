/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

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
