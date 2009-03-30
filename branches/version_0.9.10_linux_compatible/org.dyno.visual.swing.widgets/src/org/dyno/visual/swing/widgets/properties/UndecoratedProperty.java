package org.dyno.visual.swing.widgets.properties;

import java.awt.Component;

import org.dyno.visual.swing.base.WidgetProperty;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.IStructuredSelection;

public class UndecoratedProperty extends WidgetProperty {
	@Override
	public void setPropertyValue(IStructuredSelection bean, Object value) {
		super.setPropertyValue(bean, value);
		Component frame=(Component) bean.getFirstElement();
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(frame);
		adapter.resetDesignerRoot();
	}
}
