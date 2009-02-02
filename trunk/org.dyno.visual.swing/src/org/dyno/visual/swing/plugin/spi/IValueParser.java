package org.dyno.visual.swing.plugin.spi;

import java.util.List;

@SuppressWarnings("unchecked")
public interface IValueParser {
	Object parseValue(Object oldValue, List args);
}
