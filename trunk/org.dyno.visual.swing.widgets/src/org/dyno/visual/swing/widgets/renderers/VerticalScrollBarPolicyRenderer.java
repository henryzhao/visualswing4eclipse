package org.dyno.visual.swing.widgets.renderers;

import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.widgets.items.VerticalScrollBarPolicyItems;

public class VerticalScrollBarPolicyRenderer extends ItemProviderLabelProviderFactory {
	private static final long serialVersionUID = 1L;

	public VerticalScrollBarPolicyRenderer() {
		super(new VerticalScrollBarPolicyItems());
	}

}

