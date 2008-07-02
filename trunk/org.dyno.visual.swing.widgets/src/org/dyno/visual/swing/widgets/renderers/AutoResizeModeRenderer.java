package org.dyno.visual.swing.widgets.renderers;

import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.widgets.items.AutoResizeModeItems;

public class AutoResizeModeRenderer extends ItemProviderLabelProviderFactory {
	private static final long serialVersionUID = 1L;

	public AutoResizeModeRenderer() {
		super(new AutoResizeModeItems());
	}

}
