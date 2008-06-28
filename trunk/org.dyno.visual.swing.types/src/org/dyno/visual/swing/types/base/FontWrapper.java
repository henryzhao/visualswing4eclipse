/*
 * FontWrapper.java
 *
 * Created on 2007年8月13日, 下午9:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
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
