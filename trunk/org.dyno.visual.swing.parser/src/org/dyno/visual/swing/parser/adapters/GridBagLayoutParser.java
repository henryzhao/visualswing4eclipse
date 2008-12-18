package org.dyno.visual.swing.parser.adapters;

import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class GridBagLayoutParser extends LayoutParser {

	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		String layoutName = imports.addImport("java.awt.GridBagLayout");
		return "new " + layoutName + "()";
	}

}
