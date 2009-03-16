package org.dyno.visual.swing.widgets.design;

import java.awt.Component;
import java.awt.Point;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.JPopupMenuAdapter;

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
		return drop instanceof JMenuItem || drop instanceof JPopupMenu;
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
		List<WidgetAdapter> target = adaptable.getDropWidget();
		if (target == null)
			return false;
		if (target.size() != 1)
			return false;
		adaptable.setMascotLocation(p);
		WidgetAdapter dropAdapter = target.get(0);
		Component drop = dropAdapter.getWidget();
		if (drop instanceof JPopupMenu) {
			JPopupMenu popup = (JPopupMenu) drop;
			Component widget = adaptable.getWidget();
			if(widget instanceof JComponent){
				JComponent jcomp = (JComponent) widget;
				jcomp.setComponentPopupMenu(popup);
				popup.setInvoker(jcomp);
				Point hs = dropAdapter.getHotspotPoint();
				Point rp = new Point(p.x-hs.x, p.y-hs.y);
				SwingUtilities.convertPointToScreen(rp, jcomp);
				popup.setLocation(rp);
				((JPopupMenuAdapter)dropAdapter).showPopup();
				dropAdapter.requestNewName();
				return true;
			}
		}		
		return false;
	}
}
