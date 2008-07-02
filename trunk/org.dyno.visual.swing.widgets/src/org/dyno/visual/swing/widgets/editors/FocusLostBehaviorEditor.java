package org.dyno.visual.swing.widgets.editors;

import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.widgets.items.FocusLostBehaviorItems;

public class FocusLostBehaviorEditor extends ItemProviderCellEditorFactory{

	public FocusLostBehaviorEditor() {
		super(new FocusLostBehaviorItems());
	}
}
