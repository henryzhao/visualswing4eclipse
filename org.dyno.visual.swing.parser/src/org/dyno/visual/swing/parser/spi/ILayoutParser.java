package org.dyno.visual.swing.parser.spi;

import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public interface ILayoutParser {
	String createCode(ImportRewrite imports);
}
