package org.dyno.visual.swing.base;

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public class FactoryEditor implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;
	protected IFactoryProvider provider;
	public FactoryEditor(IFactoryProvider provider){
		this.provider = provider;
	}
	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		return new FactoryCellEditor(bean, parent, provider);
	}

	@Override
	public Object decodeValue(Object value) {
		return value;
	}

	@Override
	public Object encodeValue(Object value) {
		return value;
	}
}
