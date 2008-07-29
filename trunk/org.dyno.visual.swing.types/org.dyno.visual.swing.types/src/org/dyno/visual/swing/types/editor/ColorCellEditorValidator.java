/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor;

import java.util.StringTokenizer;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.graphics.RGB;

public class ColorCellEditorValidator implements ICellEditorValidator {

	@Override
	public String isValid(Object value) {
		if (value == null)
			return null;
		if (value instanceof RGB)
			return null;
		if (value instanceof String) {
			String string = (String) value;
			string = string.trim();
			if(string.equals("null"))
				return null;
			if (string.length() < 7)
				return "Incorrect color format: (red, green, blue)";
			char c = string.charAt(0);
			if (c != '(')
				return "Incorrect color format: (red, green, blue)";
			c = string.charAt(string.length() - 1);
			if (c != ')')
				return "Incorrect color format: (red, green, blue)";
			string = string.substring(1, string.length() - 1);
			StringTokenizer tokenizer = new StringTokenizer(string, ",");
			if (!tokenizer.hasMoreTokens())
				return "Incorrect color format: (red, green, blue)";
			String sRed = tokenizer.nextToken().trim();
			try {
				int r = Integer.parseInt(sRed);
				if (r < 0 || r > 255)
					return "Incorrect color format, red should be between 0 and 255";
			} catch (NumberFormatException nfe) {
				return "Incorrect color format: (red, green, blue)";
			}
			if (!tokenizer.hasMoreTokens())
				return "Incorrect color format: (red, green, blue)";
			String sGreen = tokenizer.nextToken().trim();
			try {
				int g = Integer.parseInt(sGreen);
				if (g < 0 || g > 255)
					return "Incorrect color format, green should be between 0 and 255";
			} catch (NumberFormatException nfe) {
				return "Incorrect color format: (red, green, blue)";
			}
			if (!tokenizer.hasMoreTokens())
				return "Incorrect color format: (red, green, blue)";
			String sBlue = tokenizer.nextToken().trim();
			try {
				int b = Integer.parseInt(sBlue);
				if (b < 0 || b > 255)
					return "Incorrect color format, blue should be between 0 and 255";
			} catch (NumberFormatException nfe) {
				return "Incorrect color format: (red, green, blue)";
			}
		}
		return null;
	}

}
