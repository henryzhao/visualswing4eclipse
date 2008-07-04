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

public class RectangleCellEditorValidator implements ICellEditorValidator {

	@Override
	public String isValid(Object value) {
		String string = (String) value;
		string = string.trim();
		if (string.length() < 9)
			return "Incorrect format: (x, y, width, height)";
		char c = string.charAt(0);
		if (c != '(')
			return "Incorrect format: (x, y, width, height)";
		c = string.charAt(string.length() - 1);
		if (c != ')')
			return "Incorrect format: (x, y, width, height)";
		string=string.substring(1, string.length()-1);
		StringTokenizer tokenizer = new StringTokenizer(string, ",");
		if(!tokenizer.hasMoreTokens())
			return "Incorrect format: (x, y, width, height)";
		String token = tokenizer.nextToken().trim();
		try{
			int x = Integer.parseInt(token);
			if(x<0)
				return "X must not be less than 0";
		}catch(NumberFormatException nfe){
			return "Incorrect format: (x, y, width, height)";
		}
		if(!tokenizer.hasMoreTokens())
			return "Incorrect format: (x, y, width, height)";
		token = tokenizer.nextToken().trim();
		try{
			int y = Integer.parseInt(token);
			if(y<0)
				return "Y must not be less than 0";
		}catch(NumberFormatException nfe){
			return "Incorrect format: (x, y, width, height)";
		}
		if(!tokenizer.hasMoreTokens())
			return "Incorrect format: (x, y, width, height)";
		token = tokenizer.nextToken().trim();
		try{
			int w = Integer.parseInt(token);
			if(w<0)
				return "width must not be less than 0";
		}catch(NumberFormatException nfe){
			return "Incorrect format: (x, y, width, height)";
		}
		if(!tokenizer.hasMoreTokens())
			return "Incorrect format: (x, y, width, height)";
		token = tokenizer.nextToken().trim();
		try{
			int h = Integer.parseInt(token);
			if(h<0)
				return "height must not be less than 0";
		}catch(NumberFormatException nfe){
			return "Incorrect format: (x, y, width, height)";
		}
		return null;
	}
}
