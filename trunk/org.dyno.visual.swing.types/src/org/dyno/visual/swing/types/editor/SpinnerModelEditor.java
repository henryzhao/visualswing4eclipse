package org.dyno.visual.swing.types.editor;

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.dyno.visual.swing.types.endec.SpinnerModelWrapper;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public class SpinnerModelEditor extends SpinnerModelWrapper implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		return new SpinnerModelCellEditor(parent);
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