
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

import java.util.StringTokenizer;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class DimensionCellEditorValidator implements ICellEditorValidator {

	@Override
	public String isValid(Object value) {
		String string = (String) value;
		string = string.trim();
		if (string.length() < 5)
			return Messages.DimensionCellEditorValidator_Incorrect_Format;
		char c = string.charAt(0);
		if (c != '(')
			return Messages.DimensionCellEditorValidator_Incorrect_Format;
		c = string.charAt(string.length() - 1);
		if (c != ')')
			return Messages.DimensionCellEditorValidator_Incorrect_Format;
		string=string.substring(1, string.length()-1);
		StringTokenizer tokenizer = new StringTokenizer(string, ","); //$NON-NLS-1$
		if(!tokenizer.hasMoreTokens())
			return Messages.DimensionCellEditorValidator_Incorrect_Format;
		String token = tokenizer.nextToken().trim();
		try{
			int w = Integer.parseInt(token);
			if(w<0)
				return Messages.DimensionCellEditorValidator_Incorrect_Format_Width_GT_0;
		}catch(NumberFormatException nfe){
			return Messages.DimensionCellEditorValidator_Incorrect_Format;
		}
		if(!tokenizer.hasMoreTokens())
			return Messages.DimensionCellEditorValidator_Incorrect_Format;
		token = tokenizer.nextToken().trim();
		try{
			int h = Integer.parseInt(token);
			if(h<0)
				return Messages.DimensionCellEditorValidator_Incorrect_Format_Height_GT_0;
		}catch(NumberFormatException nfe){
			return Messages.DimensionCellEditorValidator_Incorrect_Format;
		}
		return null;
	}
}

