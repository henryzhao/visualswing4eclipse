package org.dyno.visual.swing.plugin.spi;


public interface ILookAndFeelAdapter {
	@SuppressWarnings("unchecked")
	Object getDefaultValue(Class widgetClass, String propertyName);
}
