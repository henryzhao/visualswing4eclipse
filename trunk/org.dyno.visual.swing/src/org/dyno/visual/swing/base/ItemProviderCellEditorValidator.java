package org.dyno.visual.swing.base;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class ItemProviderCellEditorValidator implements ICellEditorValidator {
	private ItemProvider provider;

	public ItemProviderCellEditorValidator(ItemProvider provider) {
		this.provider = provider;
	}

	@Override
	public String isValid(Object value) {
		if (value == null)
			return null;
		int index = ((Integer) value).intValue();
		if (index < 0 || index >= provider.getItems().length)
			return "Cannot find such item:{0}!";
		return null;
	}
}
