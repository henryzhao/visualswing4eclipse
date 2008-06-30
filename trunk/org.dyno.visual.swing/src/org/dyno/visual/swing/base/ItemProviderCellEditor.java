package org.dyno.visual.swing.base;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

public class ItemProviderCellEditor extends ComboBoxCellEditor {
	public ItemProviderCellEditor(Composite parent, ItemProvider provider){
		Item[]items=provider.getItems();
		String[]names=new String[items.length];
		for(int i=0;i<items.length;i++){
			names[i]=items[i].getName();
		}
		create(parent);
		setItems(names);
		setValidator(new ItemProviderCellEditorValidator(provider));
	}
}
