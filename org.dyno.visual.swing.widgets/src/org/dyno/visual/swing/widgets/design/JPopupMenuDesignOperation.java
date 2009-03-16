package org.dyno.visual.swing.widgets.design;

import java.awt.Component;
import java.awt.Point;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.event.MouseInputListener;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.JMenuAdapter;
import org.dyno.visual.swing.widgets.JPopupMenuAdapter;

public class JPopupMenuDesignOperation extends CompositeDesignOperation {
	private int insert_y;

	public int getInsert_y() {
		return insert_y;
	}

	private int insert_index;

	private int calculateInsert(Point p) {
		JPopupMenu popup = (JPopupMenu) adaptable.getWidget();
		Component invoker = popup.getInvoker();
		if (invoker instanceof JMenu) {
			JMenu jmenu = (JMenu) popup.getInvoker();
			int count = jmenu.getMenuComponentCount();
			int caly = 0;
			for (int i = 0; i < count; i++) {
				Component jmu = jmenu.getMenuComponent(i);
				if (p.y >= caly - jmu.getHeight() / 2 && p.y < caly + jmu.getHeight() / 2) {
					insert_index = i;
					insert_y = caly;
					return caly;
				}
				caly += jmu.getHeight();
			}
			insert_index = -1;
			insert_y = caly;
			return caly;
		} else {
			MenuElement[] subElements = popup.getSubElements();
			int caly = 0;
			for (int i = 0; i < subElements.length; i++) {
				Component jmu = (Component) subElements[i];
				if (p.y >= caly - jmu.getHeight() / 2 && p.y < caly + jmu.getHeight() / 2) {
					insert_index = i;
					insert_y = caly;
					return caly;
				}
				caly += jmu.getHeight();
			}
			insert_index = -1;
			insert_y = caly;
			return caly;
		}
	}

	@Override
	public boolean dragEnter(Point p) {
		adaptable.setMascotLocation(p);
		insert_y = calculateInsert(p);
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		adaptable.setMascotLocation(p);
		return true;
	}

	@Override
	public boolean dragOver(Point p) {
		adaptable.setMascotLocation(p);
		insert_y = calculateInsert(p);
		return true;
	}

	@Override
	public boolean drop(Point p) {
		adaptable.setMascotLocation(p);
		List<WidgetAdapter> targets = adaptable.getDropWidget();
		WidgetAdapter target = targets.get(0);
		if (targets.size() != 1)
			return false;
		JPopupMenu popup = (JPopupMenu) adaptable.getWidget();
		Component invoker = popup.getInvoker();
		if (invoker instanceof JMenu) {
			JMenu jmenu = (JMenu) invoker;
			JMenuAdapter jma = (JMenuAdapter) WidgetAdapter.getWidgetAdapter(jmenu);
			if (insert_index == -1)
				jmenu.add(target.getParentContainer());
			else
				jmenu.add(target.getParentContainer(), insert_index);
			target.requestNewName();
			MouseInputListener l = (MouseInputListener) jma.getAdapter(MouseInputListener.class);
			if (l != null) {
				l.mousePressed(null);
				l.mousePressed(null);
			}
		} else {
			JPopupMenuAdapter popupAdapter = (JPopupMenuAdapter) WidgetAdapter.getWidgetAdapter(popup);
			if (insert_index == -1)
				popup.add(target.getParentContainer());
			else
				popup.add(target.getParentContainer(), insert_index);
			target.requestNewName();
			popupAdapter.hidePopup();
			popupAdapter.showPopup();
		}
		return true;
	}
}
