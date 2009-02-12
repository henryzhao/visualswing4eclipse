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
import java.util.Stack;

import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.MenuSelectionManager;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.IDesignOperation;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.design.JMenuDesignOperation;

@SuppressWarnings("unchecked")
public class JMenuAdapter extends CompositeAdapter {
	public JMenuAdapter() {
		super(null);
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
	public String getWidgetCodeClassName() {
		return JMenu.class.getName();
	}

	public boolean isMoveable() {
		return true;
	}

	public boolean isResizable() {
		return false;
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
				widgetPressed();
				widgetPressed();
			}});
	}
	void widgetPressed(){
		MouseInputListener l=(MouseInputListener) getAdapter(MouseInputListener.class);
		if(l!=null)
			l.mousePressed(null);
	}
	public void showPopup() {
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

	public void hidePopup() {
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
		if (!stack.isEmpty())
			stack.pop();
		jmenu.setPopupMenuVisible(false);
		jmenu.setSelected(false);
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
						if (stack.getClassName().indexOf("MouseGrabber") != -1 //$NON-NLS-1$
								&& stack.getMethodName().equals(
										"cancelPopupMenu")) { //$NON-NLS-1$
							return;
						}
					}
				}
				super.setPopupMenuVisible(b);
			}

		};
		requestGlobalNewName();
		menu.setText(getName());
		WidgetAdapter menuAdapter = ExtensionRegistry
				.createWidgetAdapter(JMenuItem.class);
		JMenuItem jmenu = (JMenuItem) menuAdapter.getWidget();
		menu.add(jmenu);		
		menu.setSize(menu.getPreferredSize());
		menu.doLayout();
		return menu;
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
	protected Component newWidget() {
		return new JMenu();
	}

	@Override
	public boolean needGlobalGraphics() {
		JMenuDesignOperation operation = (JMenuDesignOperation) getAdapter(IDesignOperation.class);
		return operation.isInside_popup();
	}



	@Override
	public boolean removeChild(Component child) {
		boolean success = super.removeChild(child);
		widgetPressed();
		widgetPressed();
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
}

