package org.dyno.visual.swing.widgets.design;

import java.awt.Component;
import java.awt.Point;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class CompositeDesignOperation extends WidgetDesignOperation {
	@Override
	public boolean dragEnter(Point p) {
		adaptable.setMascotLocation(p);
		return true;
	}

	protected boolean isDroppingMenuItem() {
		List<WidgetAdapter> target = adaptable.getDropWidget();
		if (target == null)
			return false;
		if (target.size() != 1)
			return false;
		Component drop = target.get(0).getWidget();
		return drop instanceof JMenuItem;
	}

	protected boolean isDroppingMenu() {
		List<WidgetAdapter> targets = adaptable.getDropWidget();
		if(targets.size()!=1)
			return false;
		Component drop = targets.get(0).getWidget();
		return drop instanceof JMenu;
	}

	protected boolean isDroppingMenuBar() {
		List<WidgetAdapter> target = adaptable.getDropWidget();
		if (target == null)
			return false;
		if (target.size() != 1)
			return false;
		Component drop = target.get(0).getWidget();
		return drop instanceof JMenuBar;
	}

	@Override
	public boolean dragExit(Point p) {
		adaptable.setMascotLocation(p);
		return true;
	}

	@Override
	public boolean dragOver(Point p) {
		adaptable.setMascotLocation(p);
		return true;
	}

	@Override
	public boolean drop(Point p) {
		if(isDroppingPopup())
			return super.drop(p);
		return false;
	}
}
