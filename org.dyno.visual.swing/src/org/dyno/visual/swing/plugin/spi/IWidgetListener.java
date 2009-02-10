package org.dyno.visual.swing.plugin.spi;

public interface IWidgetListener {
	void widgetAdded(WidgetEvent event);
	void widgetRemoved(WidgetEvent event);
	void widgetReshaped(WidgetEvent event);
}
