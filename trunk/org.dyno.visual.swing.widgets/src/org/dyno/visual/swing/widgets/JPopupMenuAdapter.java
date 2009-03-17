/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.MenuSelectionManager;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.StructuredSelection;

@SuppressWarnings("unchecked")
public class JPopupMenuAdapter extends CompositeAdapter {
	public JPopupMenuAdapter() {
		super(null);
	}

	@Override
	public Component cloneWidget() {
		JPopupMenu copy = (JPopupMenu) super.cloneWidget();
		JPopupMenu origin = (JPopupMenu) getWidget();
		int count = origin.getComponentCount();
		for (int i = 0; i < count; i++) {
			Component child = origin.getComponent(i);
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			copy.add(adapter.cloneWidget());
		}
		return copy;
	}

	@Override
	public String getWidgetCodeClassName() {
		return JPopupMenu.class.getName();
	}

	public boolean isMoveable() {
		return true;
	}

	public boolean isResizable() {
		return false;
	}

	public void showPopup() {
		JPopupMenu jpm = (JPopupMenu) getWidget();
		Stack<MenuElement> stack = MenuSelectionManager.defaultManager().getSelectionStack();
		while (!stack.isEmpty()) {
			MenuElement ele = stack.peek();
			if (ele instanceof JMenu) {
				JMenu jme = (JMenu) ele;
				jme.setPopupMenuVisible(false);
				jme.setSelected(false);
			} else if (ele instanceof JPopupMenu) {
				JPopupMenu pop = (JPopupMenu) ele;
				pop.setVisible(false);
			}
			stack.pop();
		}
		stack.add(jpm);
		jpm.setVisible(true);
		setSelected(true);
	}

	public void hidePopup() {
		JPopupMenu jpm = (JPopupMenu) getWidget();
		Stack<MenuElement> stack = MenuSelectionManager.defaultManager().getSelectionStack();
		while (!stack.isEmpty() && stack.peek() != jpm) {
			MenuElement me = stack.pop();
			if (me instanceof JMenu) {
				JMenu jme = (JMenu) me;
				jme.setPopupMenuVisible(false);
				jme.setSelected(false);
			} else if (me instanceof JPopupMenu) {
				JPopupMenu pop = (JPopupMenu) me;
				pop.setVisible(false);
			}
		}
		if (!stack.isEmpty())
			stack.pop();
		jpm.setVisible(false);
		setSelected(false);
	}

	@Override
	public String getBasename() {
		String className = getWidgetClass().getName();
		int dot = className.lastIndexOf('.');
		if (dot != -1)
			className = className.substring(dot + 1);
		return Character.toLowerCase(className.charAt(0)) + className.substring(1);
	}

	@Override
	public Component getChild(int index) {
		JPopupMenu origin = (JPopupMenu) getWidget();
		return origin.getComponent(index);
	}

	@Override
	public int getChildCount() {
		JPopupMenu origin = (JPopupMenu) getWidget();
		return origin.getComponentCount();
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
		JPopupMenu menu = new JPopupMenu() {
			private static final long serialVersionUID = 1L;

			@Override
			public void setVisible(boolean b) {
				if (!b) {
					StackTraceElement[] trace = Thread.currentThread().getStackTrace();
					for (StackTraceElement stack : trace) {
						if (stack.getClassName().indexOf("MouseGrabber") != -1 //$NON-NLS-1$
								&& stack.getMethodName().equals("cancelPopupMenu")) { //$NON-NLS-1$
							return;
						}
					}
				}
				super.setVisible(b);
			}
		};
		WidgetAdapter wa = ExtensionRegistry.createWidgetAdapter(JMenuItem.class);
		JMenuItem jmi = (JMenuItem) wa.getWidget();
		menu.add(jmi);
		menu.setSize(menu.getPreferredSize());
		menu.doLayout();
		return menu;
	}

	@Override
	protected Component newWidget() {
		return new JPopupMenu();
	}

	public CompositeAdapter getParentAdapter() {
		JPopupMenu jpopup = (JPopupMenu) getWidget();
		Component parent = jpopup.getInvoker();
		if (parent != null) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(parent);
			if (adapter != null) {
				if (adapter instanceof CompositeAdapter)
					return (CompositeAdapter) adapter;
				return adapter.getParentAdapter();
			}
		}
		return null;
	}

	@Override
	public Class getWidgetClass() {
		return JPopupMenu.class;
	}

	public void attachWidget(Component attached) {
		if (this.widget == null || this.widget.getClass() == getWidgetClass()) {
			super.attachWidget(attached);
		} else {
			ArrayList<IWidgetPropertyDescriptor> properties = getPropertyDescriptors();
			for (IWidgetPropertyDescriptor property : properties) {
				if (property.isPropertySet(getLnfClassname(), new StructuredSelection(getWidget()))) {
					if (property.getId().equals("bounds")) {
						Container widgetCon = getWidget().getParent();
						if (widgetCon == null || widgetCon.getLayout() != null) {
							continue;
						}
					}
					property.cloneProperty(attached, this.widget);
				}
			}
			JPopupMenu menu = (JPopupMenu) attached;
			Component[] menuComponents = menu.getComponents();
			((JPopupMenu) widget).removeAll();
			for (int i = 0; i < menuComponents.length; i++) {
				((JPopupMenu) widget).add(menuComponents[i]);
			}
			menu.setPreferredSize(menu.getPreferredSize());
			menu.doLayout();
			this.hotspotPoint = new Point(attached.getWidth() / 2, attached.getHeight() / 2);
			attach();
		}
	}
}
