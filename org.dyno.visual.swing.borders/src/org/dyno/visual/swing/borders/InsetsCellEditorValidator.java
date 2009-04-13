
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

package org.dyno.visual.swing.borders;

import java.awt.Insets;
import java.util.StringTokenizer;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class InsetsCellEditorValidator implements ICellEditorValidator {

	
	public String isValid(Object value) {
		if(value instanceof Insets)
			return null;
		String string = (String) value;
		string = string.trim();
		if (string.length() < 9)
			return Messages.InsetsCellEditorValidator_Incorrect_Format_Warning;
		char c = string.charAt(0);
		if (c != '(')
			return Messages.InsetsCellEditorValidator_Incorrect_Format_Warning;
		c = string.charAt(string.length() - 1);
		if (c != ')')
			return Messages.InsetsCellEditorValidator_Incorrect_Format_Warning;
		string=string.substring(1, string.length()-1);
		StringTokenizer tokenizer = new StringTokenizer(string, ","); //$NON-NLS-1$
		if(!tokenizer.hasMoreTokens())
			return Messages.InsetsCellEditorValidator_Incorrect_Format_Warning;
		String token = tokenizer.nextToken().trim();
		try{
			int x = Integer.parseInt(token);
			if(x<0)
				return Messages.InsetsCellEditorValidator_Top_Gap_GT_0;
		}catch(NumberFormatException nfe){
			return Messages.InsetsCellEditorValidator_Incorrect_Format_Warning;
		}
		if(!tokenizer.hasMoreTokens())
			return Messages.InsetsCellEditorValidator_Incorrect_Format_Warning;
		token = tokenizer.nextToken().trim();
		try{
			int y = Integer.parseInt(token);
			if(y<0)
				return Messages.InsetsCellEditorValidator_Left_Gap_GT_0;
		}catch(NumberFormatException nfe){
			return Messages.InsetsCellEditorValidator_Incorrect_Format_Warning;
		}
		if(!tokenizer.hasMoreTokens())
			return Messages.InsetsCellEditorValidator_Incorrect_Format_Warning;
		token = tokenizer.nextToken().trim();
		try{
			int w = Integer.parseInt(token);
			if(w<0)
				return Messages.InsetsCellEditorValidator_Bottom_Gap_GT_0;
		}catch(NumberFormatException nfe){
			return Messages.InsetsCellEditorValidator_Incorrect_Format_Warning;
		}
		if(!tokenizer.hasMoreTokens())
			return Messages.InsetsCellEditorValidator_Incorrect_Format_Warning;
		token = tokenizer.nextToken().trim();
		try{
			int h = Integer.parseInt(token);
			if(h<0)
				return Messages.InsetsCellEditorValidator_Right_Gap_GT_0;
		}catch(NumberFormatException nfe){
			return Messages.InsetsCellEditorValidator_Incorrect_Format_Warning;
		}
		return null;
	}
}

