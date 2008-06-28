package org.dyno.visual.swing.plugin.spi;

import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public interface ICodeGen {
	String getInitJavaCode(Object value, ImportRewrite imports);
	String getJavaCode(Object value, ImportRewrite imports);
}
