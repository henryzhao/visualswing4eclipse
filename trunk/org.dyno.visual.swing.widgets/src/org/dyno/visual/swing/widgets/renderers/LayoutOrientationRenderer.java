package org.dyno.visual.swing.widgets.renderers;

import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.widgets.items.LayoutOrientationItems;

public class LayoutOrientationRenderer extends ItemProviderLabelProviderFactory {
	private static final long serialVersionUID = 1L;

	public LayoutOrientationRenderer() {
		super(new LayoutOrientationItems());
	}

}

