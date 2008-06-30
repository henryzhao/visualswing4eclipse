/*
 * DimensionWrapper.java
 *
 * Created on August 14, 2007, 6:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.dyno.visual.swing.types.endec;

import java.awt.Dimension;

import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

/**
 * 
 * @author William Chen
 */
public class DimensionWrapper implements ICodeGen {
	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return "null";
		else {
			Dimension dim = (Dimension) value;
			String str=imports.addImport("java.awt.Dimension");
			return "new "+str+"(" + dim.width + ", " + dim.height + ")";
		}
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}
}
