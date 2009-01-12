package org.dyno.visual.swing.parser.spi;

import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public interface IPropertyCodeGenerator {
	String getJavaCode(Object bean, ImportRewrite imports);
}
