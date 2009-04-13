
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

package org.dyno.visual.swing.types.base;

import java.awt.Font;

import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

/**
 * 
 * @author William Chen
 */
public class FontWrapper implements ICodeGen {
	
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

	
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}
}

