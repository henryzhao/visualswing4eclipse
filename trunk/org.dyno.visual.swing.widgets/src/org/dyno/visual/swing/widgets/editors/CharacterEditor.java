package org.dyno.visual.swing.widgets.editors;

import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.widgets.items.CharacterItems;

public class CharacterEditor extends ItemProviderCellEditorFactory{

	public CharacterEditor() {
		super(new CharacterItems());
	}
}
