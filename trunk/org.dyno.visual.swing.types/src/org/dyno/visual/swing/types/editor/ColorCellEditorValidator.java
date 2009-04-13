
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
import org.eclipse.swt.graphics.RGB;

public class ColorCellEditorValidator implements ICellEditorValidator {

	
	public String isValid(Object value) {
		if (value == null)
			return null;
		if (value instanceof RGB)
			return null;
		if (value instanceof String) {
			String string = (String) value;
			string = string.trim();
			if(string.equals("null")) //$NON-NLS-1$
				return null;
			if (string.length() < 7)
				return Messages.ColorCellEditorValidator_Incorrect_Format;
			char c = string.charAt(0);
			if (c != '(')
				return Messages.ColorCellEditorValidator_Incorrect_Format;
			c = string.charAt(string.length() - 1);
			if (c != ')')
				return Messages.ColorCellEditorValidator_Incorrect_Format;
			string = string.substring(1, string.length() - 1);
			StringTokenizer tokenizer = new StringTokenizer(string, ","); //$NON-NLS-1$
			if (!tokenizer.hasMoreTokens())
				return Messages.ColorCellEditorValidator_Incorrect_Format;
			String sRed = tokenizer.nextToken().trim();
			try {
				int r = Integer.parseInt(sRed);
				if (r < 0 || r > 255)
					return Messages.ColorCellEditorValidator_Incorrect_Format_Red_Range;
			} catch (NumberFormatException nfe) {
				return Messages.ColorCellEditorValidator_Incorrect_Format;
			}
			if (!tokenizer.hasMoreTokens())
				return Messages.ColorCellEditorValidator_Incorrect_Format;
			String sGreen = tokenizer.nextToken().trim();
			try {
				int g = Integer.parseInt(sGreen);
				if (g < 0 || g > 255)
					return Messages.ColorCellEditorValidator_Incorrect_Format_Green_Range;
			} catch (NumberFormatException nfe) {
				return Messages.ColorCellEditorValidator_Incorrect_Format;
			}
			if (!tokenizer.hasMoreTokens())
				return Messages.ColorCellEditorValidator_Incorrect_Format_Range;
			String sBlue = tokenizer.nextToken().trim();
			try {
				int b = Integer.parseInt(sBlue);
				if (b < 0 || b > 255)
					return Messages.ColorCellEditorValidator_Incorrect_Format_Blue_Range;
			} catch (NumberFormatException nfe) {
				return Messages.ColorCellEditorValidator_Incorrect_Format;
			}
		}
		return null;
	}

}

