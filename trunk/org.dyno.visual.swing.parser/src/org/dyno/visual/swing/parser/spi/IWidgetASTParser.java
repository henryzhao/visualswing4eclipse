package org.dyno.visual.swing.parser.spi;

import org.eclipse.jdt.core.dom.TypeDeclaration;

public interface IWidgetASTParser {
	void parse(String lnfClassname, TypeDeclaration type);
}
