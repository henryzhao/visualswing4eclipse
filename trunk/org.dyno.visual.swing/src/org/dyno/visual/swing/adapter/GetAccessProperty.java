package org.dyno.visual.swing.adapter;

import org.dyno.visual.swing.base.PropertyAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;

public class GetAccessProperty extends PropertyAdapter {
	private AccessEditor editorFactory = new AccessEditor();
	private AccessRenderer rendererFactory = new AccessRenderer();
	private WidgetAdapter adapter;

	public GetAccessProperty(WidgetAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public Object getPropertyValue(Object bean) {
		return adapter.getGetAccess();
	}

	@Override
	public void setPropertyValue(Object bean, Object value) {
		adapter.setGetAccess(value == null ? WidgetAdapter.ACCESS_PRIVATE
				: (Integer) value);
		adapter.setDirty(true);
		adapter.changeNotify();
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return editorFactory.createPropertyEditor(adapter, parent);
	}

	@Override
	public String getCategory() {
		return "Code";
	}

	@Override
	public String getDisplayName() {
		return "Access Method Modifier";
	}

	@Override
	public Object getId() {
		return "get_access";
	}

	@Override
	public ILabelProvider getLabelProvider() {
		return rendererFactory.getLabelProvider();
	}
}
