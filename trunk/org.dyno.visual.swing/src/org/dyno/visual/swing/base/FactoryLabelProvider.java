package org.dyno.visual.swing.base;

import org.eclipse.jface.viewers.LabelProvider;

public class FactoryLabelProvider extends LabelProvider {
	private FactoryItem[] items;
	private IFactoryProvider provider;

	public FactoryLabelProvider(IFactoryProvider provider) {
		this.provider = provider;
		this.items = provider.getItems();
	}

	@Override
	public String getText(Object element) {
		for (int i = 0; i < items.length; i++) {
			if (provider.isSelected(items[i], element)) {
				return items[i].getObjectName();
			}
		}
		return items[0].getObjectName();
	}
}
