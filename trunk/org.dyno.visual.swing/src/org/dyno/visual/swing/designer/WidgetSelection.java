package org.dyno.visual.swing.designer;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.IStructuredSelection;

public class WidgetSelection extends ArrayList<JComponent> implements IStructuredSelection {
	private static final long serialVersionUID = -7010445264838695570L;

	public WidgetSelection(JComponent comp) {
		addSelection(comp);
	}

	private void addSelection(JComponent comp) {
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(comp);
		if (adapter == null)
			return;
		if (adapter.isSelected())
			add(comp);
		if (adapter instanceof CompositeAdapter) {
			CompositeAdapter compositeAdapter = (CompositeAdapter) adapter;
			int count = compositeAdapter.getChildCount();
			for (int i = 0; i < count; i++) {
				comp = compositeAdapter.getChild(i);
				addSelection(comp);
			}
		}
	}

	@Override
	public Object getFirstElement() {
		return isEmpty()?null:get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List toList() {
		return this;
	}
}
