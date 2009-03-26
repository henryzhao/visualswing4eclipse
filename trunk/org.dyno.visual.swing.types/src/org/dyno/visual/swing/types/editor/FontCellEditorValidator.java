
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
import org.eclipse.swt.graphics.FontData;

public class FontCellEditorValidator implements ICellEditorValidator {

	@Override
	public String isValid(Object value) {
		if (value == null)
			return null;
		if (value instanceof FontData)
			return null;
		if (value instanceof String) {
			String string = (String) value;
			string = string.trim();
			if (string.length() < 10)
				return Messages.FontCellEditorValidator_Incorrect_Format;
			char c = string.charAt(0);
			if (c != '(')
				return Messages.FontCellEditorValidator_Incorrect_Format;
			c = string.charAt(string.length() - 1);
			if (c != ')')
				return Messages.FontCellEditorValidator_Incorrect_Format;
			string = string.substring(1, string.length() - 1);
			StringTokenizer tokenizer = new StringTokenizer(string, ","); //$NON-NLS-1$
			if (!tokenizer.hasMoreTokens())
				return Messages.FontCellEditorValidator_Incorrect_Format;
			tokenizer.nextToken();
			if (!tokenizer.hasMoreTokens())
				return Messages.FontCellEditorValidator_Incorrect_Format;
			String style = tokenizer.nextToken().trim();
			if (!(style.equals("REGULAR") || style.equals("BOLD") || style.equals("ITALIC") || style.equals("BOLDITALIC"))) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				return Messages.FontCellEditorValidator_Incorrect_Format;
			if (!tokenizer.hasMoreTokens())
				return Messages.FontCellEditorValidator_Incorrect_Format;
			String size = tokenizer.nextToken().trim();
			try {
				Integer.parseInt(size);
			} catch (NumberFormatException e) {
				return Messages.FontCellEditorValidator_Incorrect_Format;
			}
		}
		return null;
	}

}

