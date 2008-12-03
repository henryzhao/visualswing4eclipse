/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Stack;

import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.base.MenuSelectionManager;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JMenuAdapter extends CompositeAdapter {
	private static int VAR_INDEX = 0;

	public JMenuAdapter() {
		super("jMenu" + (VAR_INDEX++));
	}

	@Override
	public Component cloneWidget() {
		JMenu copy = (JMenu) super.cloneWidget();
		JMenu origin = (JMenu) getWidget();
		copy.setText(origin.getText());
		int count = origin.getMenuComponentCount();
		for (int i = 0; i < count; i++) {
			Component child = origin.getMenuComponent(i);
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			copy.add(adapter.cloneWidget());
		}
		return copy;
	}

	@Override
	protected String getWidgetCodeClassName() {
		return JMenu.class.getName();
	}

	public boolean isMoveable() {
		return true;
	}

	public boolean isResizable() {
		return false;
	}

	private LabelEditor editor;

	@Override
	public IEditor getEditorAt(int x, int y) {
		if (editor == null) {
			editor = new LabelEditor();
		}
		return editor;
	}

	public void addBefore(Component hovering, Component dragged) {
		JMenu jmenu = (JMenu) getWidget();
		int index = -1;
		for (int i = 0; i < jmenu.getMenuComponentCount(); i++) {
			if (jmenu.getMenuComponent(i) == hovering) {
				index = i;
				break;
			}
		}
		if (index != -1) {
			jmenu.add(dragged, index);
		} else {
			jmenu.add(dragged);
		}
		if(jmenu.isPopupMenuVisible()){
			refreshPopup();
		}
	}
	private void refreshPopup(){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				widgetPressed(null);
				widgetPressed(null);
			}});
	}
	public void addAfter(Component hovering, Component dragged) {
		JMenu jmenu = (JMenu) getWidget();
		int index = -1;
		for (int i = 0; i < jmenu.getMenuComponentCount(); i++) {
			if (jmenu.getMenuComponent(i) == hovering) {
				index = i;
				break;
			}
		}
		if (index != -1) {
			if (index == jmenu.getMenuComponentCount() - 1)
				jmenu.add(dragged);
			else
				jmenu.add(dragged, index + 1);
		} else {
			jmenu.add(dragged);
		}
		if(jmenu.isPopupMenuVisible()){
			refreshPopup();
		}
	}

	public void addChild(Component widget) {
		JMenu jmenu = (JMenu) getWidget();
		jmenu.add(widget);
		if(jmenu.isPopupMenuVisible()){
			refreshPopup();
		}
	}

	@Override
	public Object getWidgetValue() {
		Component me = getWidget();
		JMenu jmi = (JMenu) me;
		return jmi.getText();
	}

	@Override
	public void setWidgetValue(Object value) {
		Component me = getWidget();
		JMenu jmi = (JMenu) me;
		jmi.setText(value == null ? "" : value.toString());
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = getWidget().getWidth();
		int h = getWidget().getHeight();
		Component widget = getWidget();
		FontMetrics fm = widget.getFontMetrics(widget.getFont());
		int fh = fm.getHeight() + VER_TEXT_PAD;
		Component me = getWidget();
		JMenu jmi = (JMenu) me;
		int fw = fm.stringWidth(jmi.getText()) + HOR_TEXT_PAD;
		int fx = (w - fw) / 2;
		int fy = (h - fh) / 2;
		return new Rectangle(fx, fy, fw, fh);
	}

	private static final int HOR_TEXT_PAD = 20;
	private static final int VER_TEXT_PAD = 4;

	@Override
	public boolean widgetPressed(MouseEvent e) {
		JMenu jmenu = (JMenu) getWidget();
		boolean v = jmenu.isPopupMenuVisible();
		if (v) {
			hidePopup();
		} else {
			showPopup();
		}
		return true;
	}

	void showPopup() {
		JMenu jmenu = (JMenu) getWidget();
		Container thisparent = jmenu.getParent();
		Stack<MenuElement> stack = MenuSelectionManager.defaultManager()
				.getSelectionStack();
		while (!stack.isEmpty()) {
			MenuElement ele = stack.peek();
			if (ele instanceof JMenu) {
				JMenu jme = (JMenu) ele;
				Container parent = jme.getParent();
				jme.setPopupMenuVisible(false);
				jme.setSelected(false);
				stack.pop();
				if (parent == thisparent) {
					break;
				}
			} else if (ele == thisparent) {
				break;
			} else {
				stack.pop();
			}
		}
		stack.add(jmenu);
		stack.add(jmenu.getPopupMenu());
		jmenu.setPopupMenuVisible(true);
		jmenu.setSelected(true);
	}

	void hidePopup() {
		JMenu jmenu = (JMenu) getWidget();
		Stack<MenuElement> stack = MenuSelectionManager.defaultManager()
				.getSelectionStack();
		while (!stack.isEmpty() && stack.peek() != jmenu) {
			MenuElement me = stack.pop();
			if (me instanceof JMenu) {
				JMenu jme = (JMenu) me;
				jme.setPopupMenuVisible(false);
				jme.setSelected(false);
			}
		}
		stack.pop();
		jmenu.setPopupMenuVisible(false);
		jmenu.setSelected(false);
	}

	public CompositeAdapter getParentAdapter() {
		Component me = getWidget();
		Component parent = me.getParent();
		if (parent instanceof JMenuBar)
			return (CompositeAdapter) WidgetAdapter.getWidgetAdapter(parent);
		else if (parent instanceof JPopupMenu) {
			JPopupMenu jpm = (JPopupMenu) parent;
			parent = jpm.getInvoker();
			return (CompositeAdapter) WidgetAdapter.getWidgetAdapter(parent);
		} else
			return null;
	}

	@Override
	public Component getChild(int index) {
		JMenu origin = (JMenu) getWidget();
		return origin.getMenuComponent(index);
	}

	@Override
	public int getChildCount() {
		JMenu origin = (JMenu) getWidget();
		return origin.getMenuComponentCount();
	}

	@Override
	public int getIndexOfChild(Component child) {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			if (getChild(i) == child)
				return i;
		}
		return -1;
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
	}

	@Override
	public Object getChildConstraints(Component child) {
		return null;
	}

	@Override
	protected Component createWidget() {
		JMenu menu = new JMenu() {
			private static final long serialVersionUID = 1L;

			@Override
			public void setPopupMenuVisible(boolean b) {
				if (!b) {
					StackTraceElement[] trace = Thread.currentThread()
							.getStackTrace();
					for (StackTraceElement stack : trace) {
						if (stack.getClassName().indexOf("MouseGrabber") != -1
								&& stack.getMethodName().equals(
										"cancelPopupMenu")) {
							return;
						}
					}
				}
				super.setPopupMenuVisible(b);
			}

		};
		WidgetAdapter menuAdapter = ExtensionRegistry
				.createWidgetAdapter(JMenuItem.class);
		JMenuItem jmenu = (JMenuItem) menuAdapter.getWidget();
		jmenu.setText("menu item");
		menu.add(jmenu);
		menu.setText("menu");
		menu.setSize(menu.getPreferredSize());
		menu.doLayout();
		return menu;
	}

	@Override
	protected Component newWidget() {
		return new JMenu();
	}

	private boolean isOutOfBounds(Point p) {
		JMenu jmenu = (JMenu) getWidget();
		Rectangle bounds = jmenu.getBounds();
		bounds.x = 0;
		bounds.y = 0;
		return !bounds.contains(p);
	}

	private boolean isPopupMenuVisible() {
		JMenu jmenu = (JMenu) getWidget();
		return jmenu.isPopupMenuVisible();
	}

	private boolean inside_popup = false;

	@Override
	public boolean dragEnter(Point p) {
		if (isOutOfBounds(p) && isPopupMenuVisible()) {
			inside_popup = true;
			JMenu jmenu = (JMenu) getWidget();
			JPopupMenu popup = jmenu.getPopupMenu();
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(popup);
			if (adapter == null)
				adapter = ExtensionRegistry.createWidgetAdapter(popup);
			Point sp = new Point(p);
			SwingUtilities.convertPointToScreen(sp, jmenu);
			SwingUtilities.convertPointFromScreen(sp, popup);
			adapter.dragEnter(sp);
			return true;
		} else {
			inside_popup = false;
			if (isDroppingPermitted()) {
				dropStatus = DROPPING_PERMITTED;
			} else {
				dropStatus = DROPPING_FORBIDDEN;
			}
		}
		setMascotLocation(p);
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		if (isOutOfBounds(p) && isPopupMenuVisible()) {
			inside_popup = false;
			JMenu jmenu = (JMenu) getWidget();
			JPopupMenu popup = jmenu.getPopupMenu();
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(popup);
			if (adapter == null)
				adapter = ExtensionRegistry.createWidgetAdapter(popup);
			Point sp = new Point(p);
			SwingUtilities.convertPointToScreen(sp, jmenu);
			SwingUtilities.convertPointFromScreen(sp, popup);
			adapter.dragExit(sp);
			return true;
		} else {
			inside_popup = false;
			dropStatus = NOOP;
		}
		setMascotLocation(p);
		return true;
	}

	@Override
	public boolean dragOver(Point p) {
		if (isOutOfBounds(p) && isPopupMenuVisible()) {
			inside_popup = true;
			JMenu jmenu = (JMenu) getWidget();
			JPopupMenu popup = jmenu.getPopupMenu();
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(popup);
			if (adapter == null)
				adapter = ExtensionRegistry.createWidgetAdapter(popup);
			Point sp = new Point(p);
			SwingUtilities.convertPointToScreen(sp, jmenu);
			SwingUtilities.convertPointFromScreen(sp, popup);
			adapter.dragOver(sp);
			return true;
		} else {
			inside_popup = false;
			if (isDroppingPermitted()) {
				dropStatus = DROPPING_PERMITTED;
			} else {
				dropStatus = DROPPING_FORBIDDEN;
			}
		}
		setMascotLocation(p);
		return true;
	}

	@Override
	public boolean drop(Point p) {
		if (isOutOfBounds(p) && isPopupMenuVisible()) {
			inside_popup = false;
			JMenu jmenu = (JMenu) getWidget();
			JPopupMenu popup = jmenu.getPopupMenu();
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(popup);
			if (adapter == null)
				adapter = ExtensionRegistry.createWidgetAdapter(popup);
			Point sp = new Point(p);
			SwingUtilities.convertPointToScreen(sp, jmenu);
			SwingUtilities.convertPointFromScreen(sp, popup);
			adapter.drop(sp);
			return true;
		} else {
			inside_popup = false;
			if (isDroppingPermitted()) {
				JMenu jmenu = (JMenu) getWidget();
				WidgetAdapter target = getDropWidget().get(0);
				if (target.getWidget() instanceof JMenuItem) {
					JMenuItem drop = (JMenuItem) target.getWidget();
					jmenu.add(drop);
				} else if (target.getWidget() instanceof JSeparator) {
					JSeparator jsep = (JSeparator) target.getWidget();
					jmenu.add(jsep);
				}
				clearAllSelected();
				target.setSelected(true);
				addNotify();
				if (jmenu.isPopupMenuVisible()) {
					widgetPressed(null);
					widgetPressed(null);
				} else {
					widgetPressed(null);
				}
			} else {
				Toolkit.getDefaultToolkit().beep();
			}
		}
		setMascotLocation(p);
		dropStatus = NOOP;
		return true;
	}

	@Override
	public boolean needGlobalGraphics() {
		return inside_popup;
	}

	@Override
	public void paintFocused(Graphics clipg) {
		if (inside_popup) {
			JMenu jmenu = (JMenu) getWidget();
			JPopupMenu popup = jmenu.getPopupMenu();
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(popup);
			if (adapter == null)
				adapter = ExtensionRegistry.createWidgetAdapter(popup);
			adapter.paintFocused(clipg);
		} else {
			if (dropStatus == DROPPING_FORBIDDEN) {
				JMenu jmenu = (JMenu) getWidget();
				Graphics2D g2d = (Graphics2D) clipg;
				g2d.setStroke(STROKE);
				g2d.setColor(RED_COLOR);
				g2d.drawRect(0, 0, jmenu.getWidth(), jmenu.getHeight());
			} else if (dropStatus == DROPPING_PERMITTED) {
				JMenu jmenu = (JMenu) getWidget();
				Graphics2D g2d = (Graphics2D) clipg;
				g2d.setStroke(STROKE);
				g2d.setColor(GREEN_COLOR);
				g2d.drawRect(0, 0, jmenu.getWidth(), jmenu.getHeight());
			}
		}
	}

	protected static Color RED_COLOR = new Color(255, 164, 0);
	protected static Color GREEN_COLOR = new Color(164, 255, 0);
	protected static Stroke STROKE;

	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 0, new float[] { 4 }, 0);
	}

	private boolean isDroppingPermitted() {
		List<WidgetAdapter>droppings=getDropWidget();
		if(droppings.size()!=1)
			return false;
		WidgetAdapter target = getDropWidget().get(0);
		Component drop = target.getWidget();
		return drop instanceof JMenuItem || drop instanceof JSeparator;
	}

	@Override
	public boolean removeChild(Component child) {
		boolean success = super.removeChild(child);
		widgetPressed(null);
		widgetPressed(null);
		return success;
	}
	@Override
	public Class getWidgetClass() {
		return JMenu.class;
	}
	@Override
	public IAdapter getParent() {
		JMenu jb = (JMenu) getWidget();		
		DefaultButtonModel dbm = (DefaultButtonModel) jb.getModel();
		ButtonGroup bg = dbm.getGroup();
		if (bg != null) {
			for (InvisibleAdapter invisible : getRootAdapter().getInvisibles()) {
				if (invisible instanceof ButtonGroupAdapter) {
					if (bg == ((ButtonGroupAdapter) invisible).getButtonGroup())
						return invisible;
				}
			}
		}
		return super.getParent();
	}
	@Override
	public void deleteNotify() {
		JMenu jb = (JMenu) getWidget();
		DefaultButtonModel dbm = (DefaultButtonModel) jb.getModel();
		ButtonGroup bg = dbm.getGroup();
		if(bg!=null){
			bg.remove(jb);
		}
	}	
	private int dropStatus;
	private static final int NOOP = 0;
	private static final int DROPPING_PERMITTED = 1;
	private static final int DROPPING_FORBIDDEN = 2;
}
