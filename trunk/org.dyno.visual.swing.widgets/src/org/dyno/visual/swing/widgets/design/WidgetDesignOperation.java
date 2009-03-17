package org.dyno.visual.swing.widgets.design;

import java.awt.Component;
import java.awt.Point;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.IDesignOperation;
import org.dyno.visual.swing.plugin.spi.IPainter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.JPopupMenuAdapter;
import org.eclipse.core.runtime.IAdaptable;

public class WidgetDesignOperation implements IDesignOperation, IAdaptableContext {
	protected WidgetAdapter adaptable;

	public boolean dragExit(Point p) {
		return false;
	}

	protected IPainter getPainter() {
		return (IPainter) adaptable.getAdapter(IPainter.class);
	}

	public boolean dragOver(Point p) {
		return false;
	}

	protected boolean isDroppingPopup() {
		List<WidgetAdapter> target = adaptable.getDropWidget();
		if (target == null)
			return false;
		if (target.size() != 1)
			return false;
		Component drop = target.get(0).getWidget();
		return drop instanceof JPopupMenu;
	}
	private boolean hasPopupMenu(){
		Component widget = adaptable.getWidget();
		if (widget instanceof JComponent) {
			JComponent jcomp = (JComponent) widget;
			JPopupMenu jpm = jcomp.getComponentPopupMenu();
			if(jpm!=null&&WidgetAdapter.getWidgetAdapter(jpm)!=null)
				return true;
		}
		return false;			
	}
	public boolean drop(Point p) {
		adaptable.setMascotLocation(p);
		if (isDroppingPopup()&&!hasPopupMenu()) {
			List<WidgetAdapter> target = adaptable.getDropWidget();
			WidgetAdapter dropAdapter = target.get(0);
			Component drop = dropAdapter.getWidget();
			if (drop instanceof JPopupMenu) {
				JPopupMenu popup = (JPopupMenu) drop;
				Component widget = adaptable.getWidget();
				if (widget instanceof JComponent) {
					JComponent jcomp = (JComponent) widget;
					jcomp.setComponentPopupMenu(popup);
					popup.setInvoker(jcomp);
					Point hs = dropAdapter.getHotspotPoint();
					Point rp = new Point(p.x - hs.x, p.y - hs.y);
					SwingUtilities.convertPointToScreen(rp, jcomp);
					popup.setLocation(rp);
					((JPopupMenuAdapter) dropAdapter).showPopup();
					dropAdapter.requestNewName();
					return true;
				}
			}
		}
		return false;
	}

	public boolean dragEnter(Point p) {
		return false;
	}

	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adaptable = (WidgetAdapter) adaptable;
	}
}
