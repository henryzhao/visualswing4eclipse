package org.dyno.visual.swing.widgets.editors;

import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.widgets.items.ScrollBarOrientationItems;

public class ScrollBarOrientationEditor extends ItemProviderCellEditorFactory{

	public ScrollBarOrientationEditor() {
		super(new ScrollBarOrientationItems());
	}
}
