/*
 * RectangleWrapper.java
 *
 * Created on August 14, 2007, 6:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.dyno.visual.swing.types.endec;

import java.awt.Insets;

import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

/**
 * 
 * @author William Chen
 */
public class InsetsWrapper implements ICodeGen {
	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return "null";
		Insets insets = (Insets) value;
		String str=imports.addImport("java.awt.Insets");
		return "new "+str+"(" + insets.top + ", " + insets.left + ", " + insets.bottom + ", " + insets.right + ")";
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}
}