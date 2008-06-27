package org.dyno.visual.swing.widgets.renderers;

import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.widgets.items.InternalFrameDefaultCloseOperationItems;

public class InternalFrameDefaultCloseOperationRenderer extends ItemProviderLabelProviderFactory {
	private static final long serialVersionUID = 1L;

	public InternalFrameDefaultCloseOperationRenderer() {
		super(new InternalFrameDefaultCloseOperationItems());
	}

}
