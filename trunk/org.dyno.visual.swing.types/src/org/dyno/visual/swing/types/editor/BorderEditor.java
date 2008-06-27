package org.dyno.visual.swing.types.editor;

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public class BorderEditor implements ICellEditorFactory {

	public BorderEditor() {
		super();
	}
	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		return new BorderCellEditor(bean, parent);
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
