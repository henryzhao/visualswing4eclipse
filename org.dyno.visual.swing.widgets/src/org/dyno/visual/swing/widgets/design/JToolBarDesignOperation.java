package org.dyno.visual.swing.widgets.design;

import java.awt.Component;
import java.awt.Point;

import javax.swing.JToolBar;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JToolBarDesignOperation extends CompositeDesignOperation {
	private boolean hovered;

	public boolean isHovered() {
		return hovered;
	}

	@Override
	public boolean dragEnter(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragEnter(p);
		hovered = true;
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragExit(p);
		hovered = false;
		return true;
	}

	@Override
	public boolean dragOver(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragOver(p);
		adaptable.setMascotLocation(p);
		return true;
	}

	@Override
	public boolean drop(Point p) {
		if(isDroppingPopup()||isDroppingMenuItem()||isDroppingMenuBar())
			return super.drop(p);
		JToolBar toolbar = (JToolBar) adaptable.getWidget();
		adaptable.clearAllSelected();
		for (WidgetAdapter wa : adaptable.getDropWidget()) {
			Component child = wa.getWidget();
			toolbar.add(child);
			wa.requestNewName();
			wa.setSelected(true);
		}
		adaptable.getWidget().validate();
		adaptable.setDirty(true);
		hovered = false;
		return true;
	}

}
