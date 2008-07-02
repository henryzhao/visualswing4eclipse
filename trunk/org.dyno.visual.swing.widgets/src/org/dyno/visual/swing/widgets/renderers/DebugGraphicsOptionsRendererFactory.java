package org.dyno.visual.swing.widgets.renderers;

import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.widgets.items.DebugGraphicsOptionsItems;

public class DebugGraphicsOptionsRendererFactory extends ItemProviderLabelProviderFactory {
	private static final long serialVersionUID = 1L;

	public DebugGraphicsOptionsRendererFactory() {
		super(new DebugGraphicsOptionsItems());
	}

}
