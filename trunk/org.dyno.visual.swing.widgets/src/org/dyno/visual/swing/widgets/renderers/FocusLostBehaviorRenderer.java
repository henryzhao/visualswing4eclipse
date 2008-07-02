package org.dyno.visual.swing.widgets.renderers;

import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.widgets.items.FocusLostBehaviorItems;

public class FocusLostBehaviorRenderer extends ItemProviderLabelProviderFactory {
	private static final long serialVersionUID = 1L;

	public FocusLostBehaviorRenderer() {
		super(new FocusLostBehaviorItems());
	}

}
