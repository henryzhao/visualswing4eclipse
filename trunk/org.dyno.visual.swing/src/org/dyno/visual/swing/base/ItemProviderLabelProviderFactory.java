package org.dyno.visual.swing.base;

import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.eclipse.jface.viewers.ILabelProvider;

public class ItemProviderLabelProviderFactory implements ILabelProviderFactory {
	private ItemProvider provider;
	private ILabelProvider label;

	public ItemProviderLabelProviderFactory(ItemProvider provider) {
		this.provider = provider;
	}

	@Override
	public ILabelProvider getLabelProvider() {
		if (label == null) {
			Item[] items = provider.getItems();
			String[] names = new String[items.length];
			for (int i = 0; i < names.length; i++)
				names[i] = items[i].getName();
			label = new ItemProviderLabelProvider(names);
		}
		return label;
	}
}
