package org.dyno.visual.swing.editors;

import org.dyno.visual.swing.plugin.spi.ISourceParser;
import org.dyno.visual.swing.plugin.spi.ParserFactory;

public class DefaultParserFactory extends ParserFactory {
	@Override
	public ISourceParser newParser() {
		return new DefaultSourceParser();
	}
}
