package org.dyno.visual.swing.plugin.spi;

import javax.swing.LookAndFeel;


public interface ILookAndFeelAdapter {
	@SuppressWarnings("unchecked")
	Object getDefaultValue(Class widgetClass, String propertyName);

	LookAndFeel getLookAndFeelInstance();
}
