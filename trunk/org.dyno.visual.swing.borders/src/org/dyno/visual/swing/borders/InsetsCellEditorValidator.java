package org.dyno.visual.swing.borders;

import java.awt.Insets;
import java.util.StringTokenizer;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class InsetsCellEditorValidator implements ICellEditorValidator {

	@Override
	public String isValid(Object value) {
		if(value instanceof Insets)
			return null;
		String string = (String) value;
		string = string.trim();
		if (string.length() < 9)
			return "Incorrect format: (top, left, bottom, right)";
		char c = string.charAt(0);
		if (c != '(')
			return "Incorrect format: (top, left, bottom, right)";
		c = string.charAt(string.length() - 1);
		if (c != ')')
			return "Incorrect format: (top, left, bottom, right)";
		string=string.substring(1, string.length()-1);
		StringTokenizer tokenizer = new StringTokenizer(string, ",");
		if(!tokenizer.hasMoreTokens())
			return "Incorrect format: (top, left, bottom, right)";
		String token = tokenizer.nextToken().trim();
		try{
			int x = Integer.parseInt(token);
			if(x<0)
				return "top gap must not be less than 0";
		}catch(NumberFormatException nfe){
			return "Incorrect format: (top, left, bottom, right)";
		}
		if(!tokenizer.hasMoreTokens())
			return "Incorrect format: (top, left, bottom, right)";
		token = tokenizer.nextToken().trim();
		try{
			int y = Integer.parseInt(token);
			if(y<0)
				return "left gap must not be less than 0";
		}catch(NumberFormatException nfe){
			return "Incorrect format: (top, left, bottom, right)";
		}
		if(!tokenizer.hasMoreTokens())
			return "Incorrect format: (top, left, bottom, right)";
		token = tokenizer.nextToken().trim();
		try{
			int w = Integer.parseInt(token);
			if(w<0)
				return "bottom gap must not be less than 0";
		}catch(NumberFormatException nfe){
			return "Incorrect format: (top, left, bottom, right)";
		}
		if(!tokenizer.hasMoreTokens())
			return "Incorrect format: (top, left, bottom, right)";
		token = tokenizer.nextToken().trim();
		try{
			int h = Integer.parseInt(token);
			if(h<0)
				return "right gap must not be less than 0";
		}catch(NumberFormatException nfe){
			return "Incorrect format: (top, left, bottom, right)";
		}
		return null;
	}
}
