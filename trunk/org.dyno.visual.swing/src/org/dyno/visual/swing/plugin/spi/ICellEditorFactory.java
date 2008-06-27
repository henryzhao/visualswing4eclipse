package org.dyno.visual.swing.plugin.spi;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public interface ICellEditorFactory {
	CellEditor createPropertyEditor(Object bean, Composite parent);
	Object encodeValue(Object value);
	Object decodeValue(Object value);
}
