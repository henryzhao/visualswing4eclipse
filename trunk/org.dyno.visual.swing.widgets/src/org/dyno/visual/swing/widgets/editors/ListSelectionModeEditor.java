package org.dyno.visual.swing.widgets.editors;

import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.widgets.items.ListSelectionModeItems;

public class ListSelectionModeEditor  extends ItemProviderCellEditorFactory{

	public ListSelectionModeEditor() {
		super(new ListSelectionModeItems());
	}
}
