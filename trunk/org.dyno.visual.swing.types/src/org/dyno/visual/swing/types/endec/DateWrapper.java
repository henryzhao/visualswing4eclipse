/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.endec;

import java.util.Date;

import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class DateWrapper implements ICodeGen{
	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if(value==null)
			return "null";
		String className = imports.addImport("java.util.Date");
		Date date = (Date)value;
		long time = date.getTime();
		return "new "+className+"("+time+"L)";
	}
}
