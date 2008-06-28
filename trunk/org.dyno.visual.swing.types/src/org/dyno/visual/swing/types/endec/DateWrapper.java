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
