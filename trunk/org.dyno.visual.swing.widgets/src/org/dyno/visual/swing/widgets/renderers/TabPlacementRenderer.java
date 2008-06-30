package org.dyno.visual.swing.widgets.renderers;

import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.widgets.items.TabPlacementItems;

public class TabPlacementRenderer extends ItemProviderLabelProviderFactory {
	private static final long serialVersionUID = 1L;

	public TabPlacementRenderer() {
		super(new TabPlacementItems());
	}

}
