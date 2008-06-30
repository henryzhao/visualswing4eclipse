package org.dyno.visual.swing.types.editor;

import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class IntegerEditor implements ICellEditorFactory {
	private static final long serialVersionUID = -4403435758517308113L;

	@Override
	public CellEditor createPropertyEditor(Object bean, Composite parent) {
		CellEditor editor = new TextCellEditor(parent);
		editor.setValidator(new IntegerCellEditorValidator());
		return editor;
	}

	@Override
	public Object decodeValue(Object value) {
		if (value == null)
			return 0;
		else {
			String sValue = value.toString().trim();
			return Integer.valueOf(sValue);
		}
	}

	@Override
	public Object encodeValue(Object value) {
		if (value == null)
			return "0";
		else
			return value.toString();
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		return value==null?"0":value.toString();
	}
}
