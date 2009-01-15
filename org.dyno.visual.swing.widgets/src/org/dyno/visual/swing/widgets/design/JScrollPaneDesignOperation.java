package org.dyno.visual.swing.widgets.design;

import java.awt.Component;
import java.awt.Point;
import java.util.List;

import javax.swing.JScrollPane;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JScrollPaneDesignOperation extends CompositeDesignOperation {
	public boolean isPermitted() {
		List<WidgetAdapter>adapters=adaptable.getDropWidget();
		if(adapters.size()!=1)
			return false;		
		Component comp = ((JScrollPane) adaptable.getWidget()).getViewport().getView();
		return comp == null;
	}

	@Override
	public boolean dragOver(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragOver(p);
		adaptable.setMascotLocation(p);
		return true;
	}

	private boolean hovered;

	public boolean isHovered() {
		return hovered;
	}

	@Override
	public boolean dragEnter(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragEnter(p);
		adaptable.setMascotLocation(p);
		hovered = true;
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragExit(p);
		adaptable.setMascotLocation(p);
		hovered = false;
		return true;
	}

	@Override
	public boolean drop(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.drop(p);
		adaptable.setMascotLocation(p);
		if (isPermitted()) {
			JScrollPane jsp = (JScrollPane) adaptable.getWidget();
			WidgetAdapter todrop = adaptable.getDropWidget().get(0);
			jsp.setViewportView(todrop.getWidget());
			todrop.requestNewName();
			adaptable.getRootAdapter().getWidget().validate();
			adaptable.clearAllSelected();
			todrop.setSelected(true);
		}
		hovered = false;
		return true;
	}

}
