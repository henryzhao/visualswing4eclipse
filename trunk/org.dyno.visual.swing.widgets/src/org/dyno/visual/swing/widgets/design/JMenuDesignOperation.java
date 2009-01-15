package org.dyno.visual.swing.widgets.design;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.IDesignOperation;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JMenuDesignOperation extends CompositeDesignOperation {
	private int dropStatus;
	private boolean inside_popup = false;
	public boolean isInside_popup() {
		return inside_popup;
	}

	public void setInside_popup(boolean inside_popup) {
		this.inside_popup = inside_popup;
	}

	public int getDropStatus() {
		return dropStatus;
	}
	public static final int NOOP = 0;
	public static final int DROPPING_PERMITTED = 1;
	public static final int DROPPING_FORBIDDEN = 2;
	private boolean isOutOfBounds(Point p) {
		JMenu jmenu = (JMenu) adaptable.getWidget();
		Rectangle bounds = jmenu.getBounds();
		bounds.x = 0;
		bounds.y = 0;
		return !bounds.contains(p);
	}

	private boolean isPopupMenuVisible() {
		JMenu jmenu = (JMenu) adaptable.getWidget();
		return jmenu.isPopupMenuVisible();
	}


	private boolean isDroppingPermitted() {
		List<WidgetAdapter>droppings=adaptable.getDropWidget();
		if(droppings.size()!=1)
			return false;
		WidgetAdapter target = adaptable.getDropWidget().get(0);
		Component drop = target.getWidget();
		return drop instanceof JMenuItem || drop instanceof JSeparator;
	}
	@Override
	public boolean dragEnter(Point p) {
		if (isOutOfBounds(p) && isPopupMenuVisible()) {
			inside_popup=true;
			JMenu jmenu = (JMenu) adaptable.getWidget();
			JPopupMenu popup = jmenu.getPopupMenu();
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(popup);
			if (adapter == null)
				adapter = ExtensionRegistry.createWidgetAdapter(popup);
			Point sp = new Point(p);
			SwingUtilities.convertPointToScreen(sp, jmenu);
			SwingUtilities.convertPointFromScreen(sp, popup);
			IDesignOperation operation = (IDesignOperation) adapter.getAdapter(IDesignOperation.class);
			operation.dragEnter(sp);
			return true;
		} else {
			inside_popup=false;
			if (isDroppingPermitted()) {
				setDropStatus(DROPPING_PERMITTED);
			} else {
				setDropStatus(DROPPING_FORBIDDEN);
			}
		}
		adaptable.setMascotLocation(p);
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		if (isOutOfBounds(p) && isPopupMenuVisible()) {
			inside_popup=false;
			JMenu jmenu = (JMenu) adaptable.getWidget();
			JPopupMenu popup = jmenu.getPopupMenu();
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(popup);
			if (adapter == null)
				adapter = ExtensionRegistry.createWidgetAdapter(popup);
			Point sp = new Point(p);
			SwingUtilities.convertPointToScreen(sp, jmenu);
			SwingUtilities.convertPointFromScreen(sp, popup);
			IDesignOperation operation = (IDesignOperation) adapter.getAdapter(IDesignOperation.class);
			operation.dragExit(sp);
			return true;
		} else {
			inside_popup=false;
			setDropStatus(NOOP);
		}
		adaptable.setMascotLocation(p);
		return true;
	}

	@Override
	public boolean dragOver(Point p) {
		if (isOutOfBounds(p) && isPopupMenuVisible()) {
			inside_popup=true;
			JMenu jmenu = (JMenu) adaptable.getWidget();
			JPopupMenu popup = jmenu.getPopupMenu();
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(popup);
			if (adapter == null)
				adapter = ExtensionRegistry.createWidgetAdapter(popup);
			Point sp = new Point(p);
			SwingUtilities.convertPointToScreen(sp, jmenu);
			SwingUtilities.convertPointFromScreen(sp, popup);
			IDesignOperation operation = (IDesignOperation) adapter.getAdapter(IDesignOperation.class);
			operation.dragOver(sp);
			return true;
		} else {
			inside_popup=false;
			if (isDroppingPermitted()) {
				setDropStatus(DROPPING_PERMITTED);
			} else {
				setDropStatus(DROPPING_FORBIDDEN);
			}
		}
		adaptable.setMascotLocation(p);
		return true;
	}
	private void setDropStatus(int dropStatus){
		this.dropStatus=dropStatus;
	}
	@Override
	public boolean drop(Point p) {
		if (isOutOfBounds(p) && isPopupMenuVisible()) {
			inside_popup=false;
			JMenu jmenu = (JMenu) adaptable.getWidget();
			JPopupMenu popup = jmenu.getPopupMenu();
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(popup);
			if (adapter == null)
				adapter = ExtensionRegistry.createWidgetAdapter(popup);
			Point sp = new Point(p);
			SwingUtilities.convertPointToScreen(sp, jmenu);
			SwingUtilities.convertPointFromScreen(sp, popup);
			IDesignOperation operation = (IDesignOperation) adapter.getAdapter(IDesignOperation.class);
			operation.drop(sp);
			return true;
		} else {
			inside_popup=false;
			if (isDroppingPermitted()) {
				JMenu jmenu = (JMenu) adaptable.getWidget();
				WidgetAdapter target = adaptable.getDropWidget().get(0);
				if (target.getWidget() instanceof JMenuItem) {
					JMenuItem drop = (JMenuItem) target.getWidget();
					jmenu.add(drop);
				} else if (target.getWidget() instanceof JSeparator) {
					JSeparator jsep = (JSeparator) target.getWidget();
					jmenu.add(jsep);
				}else{
					adaptable.setMascotLocation(p);
					setDropStatus(NOOP);
					return false;
				}
				adaptable.clearAllSelected();
				target.requestNewName();
				target.setSelected(true);
				adaptable.setDirty(true);
				adaptable.addNotify();
				if (jmenu.isPopupMenuVisible()) {
					widgetPressed();
					widgetPressed();
				} else {
					widgetPressed();
				}
			} else {
				Toolkit.getDefaultToolkit().beep();
				adaptable.setMascotLocation(p);
				setDropStatus(NOOP);
				return false;
			}
		}
		adaptable.setMascotLocation(p);
		setDropStatus(NOOP);
		return true;
	}
	void widgetPressed(){
		MouseInputListener l=(MouseInputListener) adaptable.getAdapter(MouseInputListener.class);
		if(l!=null)
			l.mousePressed(null);
	}
}
