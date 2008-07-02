package org.dyno.visual.swing.base;

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public class ItemProviderCellEditorFactory implements ICellEditorFactory {
	private ItemProvider provider;

	public ItemProviderCellEditorFactory(ItemProvider provider) {
		this.provider = provider;
	}

	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		return new ItemProviderCellEditor(parent, provider);
	}

	@Override
	public Object decodeValue(Object value) {
		if (value == null)
			value = Integer.valueOf(0);
		int index = ((Integer) value).intValue();
		Item[] items = provider.getItems();
		return items[index].getValue();
	}

	@Override
	public Object encodeValue(Object value) {
		if (value == null)
			value = Integer.valueOf(0);
		Item[] items = provider.getItems();
		for (int i = 0; i < items.length; i++) {
			if (value.equals(items[i].getValue()))
				return i;
		}
		return 0;
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			value = Integer.valueOf(0);
		Item[] items = provider.getItems();
		for (int i = 0; i < items.length; i++) {
			if (value.equals(items[i].getValue()))
				return items[i].getCode(imports);
		}
		return value.toString();
	}

}
