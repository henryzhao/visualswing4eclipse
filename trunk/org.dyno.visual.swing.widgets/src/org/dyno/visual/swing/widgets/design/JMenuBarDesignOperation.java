package org.dyno.visual.swing.widgets.design;

import java.awt.Component;
import java.awt.Point;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JMenuBarDesignOperation extends CompositeDesignOperation {
	public static final int NOOP = 0;
	public static final int DROPPING_PERMITTED = 1;
	public static final int DROPPING_FORBIDDEN = 2;
	private int insert_x;
	private int dropStatus;
	public int getInsert_x() {
		return insert_x;
	}
	public int getDropStatus() {
		return dropStatus;
	}
	@Override
	public boolean dragEnter(Point p) {
		adaptable.setMascotLocation(p);
		if (isDroppingMenu()) {
			dropStatus = DROPPING_PERMITTED;
			insert_x=calculateInsert(p);
		} else {
			dropStatus = DROPPING_FORBIDDEN;
		}
		return true;
	}
	private int calculateInsert(Point p){
		JMenuBar jmb=(JMenuBar)adaptable.getWidget();
		int count=jmb.getMenuCount();
		int calx=0;
		for(int i=0;i<count;i++){
			JMenu jmu=jmb.getMenu(i);
			if(p.x>=calx&&p.x<calx+jmu.getWidth()){
				return calx;
			}
			calx+=jmu.getWidth();
		}
		return calx;
	}
	@Override
	public boolean dragExit(Point p) {
		adaptable.setMascotLocation(p);
		dropStatus = NOOP;
		return true;
	}

	@Override
	public boolean dragOver(Point p) {
		adaptable.setMascotLocation(p);
		if (isDroppingMenu()) {
			dropStatus = DROPPING_PERMITTED;
			insert_x=calculateInsert(p);
		} else {
			dropStatus = DROPPING_FORBIDDEN;
		}
		return true;
	}

	@Override
	public boolean drop(Point p) {
		adaptable.setMascotLocation(p);
		dropStatus=NOOP;
		if (isDroppingMenu()) {
			WidgetAdapter menuAdapter = adaptable.getDropWidget().get(0);
			JMenu jmenu = (JMenu) menuAdapter.getWidget();
			JMenuBar jmb = (JMenuBar) adaptable.getWidget();
			jmb.add(jmenu);
			jmb.validate();
			jmb.doLayout();
			adaptable.clearAllSelected();
			menuAdapter.requestNewName();
			menuAdapter.setSelected(true);
			adaptable.addNotify();
			adaptable.repaintDesigner();
		} else {
		}
		return true;
	}

	private boolean isDroppingMenu() {
		List<WidgetAdapter> targets = adaptable.getDropWidget();
		if(targets.size()!=1)
			return false;
		Component drop = targets.get(0).getWidget();
		return drop instanceof JMenu;
	}
}
