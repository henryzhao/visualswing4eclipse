package org.dyno.visual.swing.widgets.renderers;

import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.widgets.items.ListSelectionModeItems;

public class ListSelectionModeRenderer extends ItemProviderLabelProviderFactory {
	private static final long serialVersionUID = 1L;

	public ListSelectionModeRenderer() {
		super(new ListSelectionModeItems());
	}

}
