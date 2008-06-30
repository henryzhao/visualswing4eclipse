package org.dyno.visual.swing.parser;

import org.dyno.visual.swing.plugin.spi.ISourceParser;
import org.dyno.visual.swing.plugin.spi.ParserFactory;

public class ASTParserFactory extends ParserFactory {

	@Override
	public ISourceParser newParser() {
		return new ASTBasedParser();
	}
}
