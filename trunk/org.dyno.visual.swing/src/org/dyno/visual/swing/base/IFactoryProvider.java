package org.dyno.visual.swing.base;

public interface IFactoryProvider {
	FactoryItem[]getItems();
	boolean isSelected(FactoryItem item, Object value);
}
