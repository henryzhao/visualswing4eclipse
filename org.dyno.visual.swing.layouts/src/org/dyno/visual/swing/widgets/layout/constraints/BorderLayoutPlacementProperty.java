package org.dyno.visual.swing.widgets.layout.constraints;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.layout.BorderLayoutAdapter;
import org.eclipse.jface.viewers.IStructuredSelection;

public class BorderLayoutPlacementProperty extends CommonConstraintsProperty {
	
	public BorderLayoutPlacementProperty(BorderLayoutAdapter adapter, Component widget){
		super(adapter, widget, new BorderLayoutConstraintEditor(), new BorderLayoutConstraintRenderer(), "placement");
	}
	@Override
	@SuppressWarnings("unchecked")
	public Class getPropertyType() {
		return String.class;
	}
	@Override
	public void setPropertyValue(IStructuredSelection bean, Object value) {
		JComponent container = adapter.getContainer();
		BorderLayout bLayout = (BorderLayout) container.getLayout();
		Object constraints = editorFactory.decodeValue(value); 
		Component comp = bLayout.getLayoutComponent(constraints);
		if(comp!=null){
			Object newConstraints = bLayout.getConstraints(widget);
			container.remove(widget);
			container.remove(comp);
			container.add(widget, constraints);
			container.add(comp, newConstraints);
		}else{
			container.remove(widget);
			container.add(widget, constraints);
		}
		container.doLayout();
		WidgetAdapter containerAdapter = WidgetAdapter.getWidgetAdapter(container);
		containerAdapter.repaintDesigner();
	}
}
