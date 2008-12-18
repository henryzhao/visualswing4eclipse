package org.dyno.visual.swing.plugin.spi;

import java.util.List;

public interface IValueParser {
	@SuppressWarnings("unchecked")
	Object parseValue(Object oldValue, List args);
}
