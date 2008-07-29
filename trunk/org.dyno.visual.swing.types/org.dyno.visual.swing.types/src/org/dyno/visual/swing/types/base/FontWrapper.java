/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.base;

import java.awt.Font;

import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

/**
 * 
 * @author William Chen
 */
public class FontWrapper implements ICodeGen {
	@Override
	public String getJavaCode(Object v, ImportRewrite imports) {
		if (v == null) {
			return "null";
		}
		Font f = (Font) v;
		String strStyle;
		int style = f.getStyle();
		if ((style & Font.BOLD) != 0) {
			if ((style & Font.ITALIC) != 0) {
				strStyle = imports.addImport("java.awt.Font")+".BOLD|java.awt.Font.ITALIC";
			} else {
				strStyle = imports.addImport("java.awt.Font")+".BOLD";
			}
		} else {
			if ((style & Font.ITALIC) != 0) {
				strStyle = imports.addImport("java.awt.Font")+".ITALIC";
			} else {
				strStyle = imports.addImport("java.awt.Font")+".PLAIN";
			}
		}
		return "new "+imports.addImport("java.awt.Font")+"(\"" + f.getFamily() + "\", " + strStyle + ", " + f.getSize() + ")";
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}
}