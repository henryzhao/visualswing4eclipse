package org.dyno.visual.swing.widgets.renderers;

import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.widgets.items.ScrollBarOrientationItems;

public class ScrollBarOrientationRenderer extends ItemProviderLabelProviderFactory {
	private static final long serialVersionUID = 1L;

	public ScrollBarOrientationRenderer() {
		super(new ScrollBarOrientationItems());
	}

}
