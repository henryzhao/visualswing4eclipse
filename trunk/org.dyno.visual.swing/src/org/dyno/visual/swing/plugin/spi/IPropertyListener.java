package org.dyno.visual.swing.plugin.spi;

public interface IPropertyListener {
	void propertyReset(PropertyEvent event);
	void propertyAdded(PropertyEvent event);
	void propertyEdited(PropertyEvent event);
}
