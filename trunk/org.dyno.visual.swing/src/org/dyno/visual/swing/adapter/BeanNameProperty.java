package org.dyno.visual.swing.adapter;

import org.dyno.visual.swing.base.PropertyAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class BeanNameProperty extends PropertyAdapter {
	private WidgetAdapter adapter;
	public BeanNameProperty(WidgetAdapter adapter){
		this.adapter = adapter;
	}
	@Override
	public Object getPropertyValue(Object bean) {
		return adapter.getName();
	}

	@Override
	public void setPropertyValue(Object bean, Object value) {
		String name = (String) value;
		adapter.setName(name);
		if (!adapter.isRoot()) {
			adapter.getParentAdapter().setDirty(true);
		}
		adapter.setDirty(true);
		adapter.changeNotify();
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		TextCellEditor editor = new TextCellEditor(parent);
		editor.setValidator(new BeanNameValidator(adapter));
		return editor;
	}

	@Override
	public String getCategory() {
		return "Code";
	}

	@Override
	public String getDisplayName() {
		return "Bean Field Name";
	}

	@Override
	public Object getId() {
		return "bean.name";
	}
}