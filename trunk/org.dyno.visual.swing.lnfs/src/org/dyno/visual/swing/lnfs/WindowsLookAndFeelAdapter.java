package org.dyno.visual.swing.lnfs;

import java.awt.Toolkit;
import java.util.HashMap;

import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;

public class WindowsLookAndFeelAdapter implements ILookAndFeelAdapter {
	@SuppressWarnings("unchecked")
	private HashMap<Class, WidgetValue> xpValues;
	@SuppressWarnings("unchecked")
	private HashMap<Class, WidgetValue> classicValues;

	@SuppressWarnings("unchecked")
	public WindowsLookAndFeelAdapter() {
		xpValues = new HashMap<Class, WidgetValue>();
		classicValues = new HashMap<Class, WidgetValue>();

	}

	private static boolean isXP() {
		Boolean xp = (Boolean) Toolkit.getDefaultToolkit().getDesktopProperty(
				"win.xpstyle.themeActive");
		return xp != null && xp.booleanValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getDefaultValue(Class widgetClass, String propertyName) {
		WidgetValue widgetValue;
		if (isXP()) {
			widgetValue = xpValues.get(widgetClass);
		} else {
			widgetValue = classicValues.get(widgetClass);
		}
		if (widgetValue != null)
			return widgetValue.get(propertyName);
		else
			return null;
	}
}
