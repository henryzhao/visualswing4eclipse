package org.dyno.visual.swing.widgets.editors;

import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.widgets.items.AutoResizeModeItems;

public class AutoResizeModeEditor extends ItemProviderCellEditorFactory{

	public AutoResizeModeEditor() {
		super(new AutoResizeModeItems());
	}
}
