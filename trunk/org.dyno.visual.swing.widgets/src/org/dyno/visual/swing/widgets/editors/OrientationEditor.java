package org.dyno.visual.swing.widgets.editors;

import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.widgets.items.OrientationItems;

public class OrientationEditor extends ItemProviderCellEditorFactory{

	public OrientationEditor() {
		super(new OrientationItems());
	}
}
