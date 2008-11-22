/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.base;

import java.awt.Color;

import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

/**
 * 
 * @author William Chen
 */
public class ColorWrapper implements ICodeGen {
	public String encode(Object v) {
		if (v == null) {
			return "null";
		}
		Color c = (Color) v;
		if (c.equals(Color.black))
			return "black";
		else if (c.equals(Color.blue))
			return "blue";
		else if (c.equals(Color.cyan))
			return "cyan";
		else if (c.equals(Color.darkGray))
			return "darkGray";
		else if (c.equals(Color.gray))
			return "gray";
		else if (c.equals(Color.green))
			return "green";
		else if (c.equals(Color.lightGray))
			return "lightGray";
		else if (c.equals(Color.magenta))
			return "magenta";
		else if (c.equals(Color.orange))
			return "orange";
		else if (c.equals(Color.pink))
			return "pink";
		else if (c.equals(Color.red))
			return "red";
		else if (c.equals(Color.white))
			return "white";
		else if (c.equals(Color.yellow))
			return "yellow";
		return "(" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + ")";
	}

	@Override
	public String getJavaCode(Object value,ImportRewrite imports) {
		if (value == null)
			return "null";
		String encodeString = encode(value);
		if (encodeString.startsWith("(")) {
			Color c = (Color) value;
			return "new "+imports.addImport("java.awt.Color")+"(" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + ")";
		} else {
			return imports.addImport("java.awt.Color")+"." + encodeString;
		}
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

}