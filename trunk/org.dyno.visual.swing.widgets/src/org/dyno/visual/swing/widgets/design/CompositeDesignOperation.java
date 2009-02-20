package org.dyno.visual.swing.widgets.design;

import java.awt.Component;
import java.awt.Point;
import java.util.List;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class CompositeDesignOperation extends WidgetDesignOperation {
	@Override
	public boolean dragEnter(Point p) {
		adaptable.setMascotLocation(p);
		return true;
	}
	
	protected boolean isDroppingMenuItem(){
		List<WidgetAdapter> target = adaptable.getDropWidget();
		if(target==null)
			return false;
		if(target.size()!=1)
			return false;
		Component drop = target.get(0).getWidget();
		return drop instanceof JMenuItem;
	}
	
	protected boolean isDroppingMenuBar(){
		List<WidgetAdapter> target = adaptable.getDropWidget();
		if(target==null)
			return false;
		if(target.size()!=1)
			return false;
		Component drop =  target.get(0).getWidget();
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
		adaptable.setMascotLocation(p);
		return false;
	}
}
