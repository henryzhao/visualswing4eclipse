package org.dyno.visual.swing.widgets.renderers;

import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.widgets.items.SplitPaneOrientationItems;

public class SplitPaneOrientationRenderer extends ItemProviderLabelProviderFactory {
	private static final long serialVersionUID = 1L;

	public SplitPaneOrientationRenderer() {
		super(new SplitPaneOrientationItems());
	}

}

