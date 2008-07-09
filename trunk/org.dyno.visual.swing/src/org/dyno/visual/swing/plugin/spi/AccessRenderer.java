package org.dyno.visual.swing.plugin.spi;

import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;

class AccessRenderer extends ItemProviderLabelProviderFactory {
	public AccessRenderer() {
		super(new AccessItems());
	}
}