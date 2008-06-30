package org.dyno.visual.swing.base;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class FactoryCellEditor extends ComboBoxCellEditor {
	private FactoryItem[] items;
	private Object[] instances;
	private IFactoryProvider provider;

	public FactoryCellEditor(Object bean, Composite parent, IFactoryProvider provider) {
		super.create(parent);
		this.provider = provider;
		this.items = provider.getItems();
		instances = new Object[items.length];
		String[] names = new String[items.length];
		for (int i = 0; i < items.length; i++) {
			names[i] = items[i].getObjectName();
			instances[i] = items[i].getFactory().newInstance(bean);
		}
		super.setItems(names);
	}

	@Override
	protected Control createControl(Composite parent) {
		CCombo combo = (CCombo) super.createControl(parent);
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				focusLost();
			}
		});
		return combo;
	}

	@Override
	protected Object doGetValue() {
		Integer index = (Integer) super.doGetValue();
		int i = index.intValue();
		if (i >= 0 && i < items.length)
			return instances[i];
		return null;
	}

	@Override
	protected void doSetValue(Object value) {
		for (int i = 0; i < items.length; i++) {
			if (provider.isSelected(items[i], value)) {
				super.doSetValue(Integer.valueOf(i));
				instances[i] = value;
				break;
			}
		}
	}
}
