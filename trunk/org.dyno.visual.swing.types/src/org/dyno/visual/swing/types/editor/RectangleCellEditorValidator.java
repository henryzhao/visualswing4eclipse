
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

public class RectangleCellEditorValidator implements ICellEditorValidator {

	
	public String isValid(Object value) {
		if(value==null)
			return null;
		String string = (String) value;
		string = string.trim();
		if (string.length() < 9)
			return Messages.RectangleCellEditorValidator_Incorrect_Format;
		char c = string.charAt(0);
		if (c != '(')
			return Messages.RectangleCellEditorValidator_Incorrect_Format;
		c = string.charAt(string.length() - 1);
		if (c != ')')
			return Messages.RectangleCellEditorValidator_Incorrect_Format;
		string=string.substring(1, string.length()-1);
		StringTokenizer tokenizer = new StringTokenizer(string, ","); //$NON-NLS-1$
		if(!tokenizer.hasMoreTokens())
			return Messages.RectangleCellEditorValidator_Incorrect_Format;
		String token = tokenizer.nextToken().trim();
		try{
			int x = Integer.parseInt(token);
			if(x<0)
				return Messages.RectangleCellEditorValidator_X_GT_0;
		}catch(NumberFormatException nfe){
			return Messages.RectangleCellEditorValidator_Incorrect_Format;
		}
		if(!tokenizer.hasMoreTokens())
			return Messages.RectangleCellEditorValidator_Incorrect_Format;
		token = tokenizer.nextToken().trim();
		try{
			int y = Integer.parseInt(token);
			if(y<0)
				return Messages.RectangleCellEditorValidator_Y_GT_0;
		}catch(NumberFormatException nfe){
			return Messages.RectangleCellEditorValidator_Incorrect_Format;
		}
		if(!tokenizer.hasMoreTokens())
			return Messages.RectangleCellEditorValidator_Incorrect_Format;
		token = tokenizer.nextToken().trim();
		try{
			int w = Integer.parseInt(token);
			if(w<0)
				return Messages.RectangleCellEditorValidator_Width_GT_0;
		}catch(NumberFormatException nfe){
			return Messages.RectangleCellEditorValidator_Incorrect_Format;
		}
		if(!tokenizer.hasMoreTokens())
			return Messages.RectangleCellEditorValidator_Incorrect_Format;
		token = tokenizer.nextToken().trim();
		try{
			int h = Integer.parseInt(token);
			if(h<0)
				return Messages.RectangleCellEditorValidator_Height_GT_0;
		}catch(NumberFormatException nfe){
			return Messages.RectangleCellEditorValidator_Incorrect_Format;
		}
		return null;
	}
}

