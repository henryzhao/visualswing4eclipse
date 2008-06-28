package org.dyno.visual.swing.widgets.editors;

import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.widgets.items.LayoutOrientationItems;

public class LayoutOrientationEditor extends ItemProviderCellEditorFactory{

	public LayoutOrientationEditor() {
		super(new LayoutOrientationItems());
	}
}
