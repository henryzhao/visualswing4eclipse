package org.dyno.visual.swing.widgets.editors;

import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.widgets.items.DebugGraphicsOptionsItems;

public class DebugGraphicsOptionsEditorFactory extends ItemProviderCellEditorFactory{

	public DebugGraphicsOptionsEditorFactory() {
		super(new DebugGraphicsOptionsItems());
	}
}
