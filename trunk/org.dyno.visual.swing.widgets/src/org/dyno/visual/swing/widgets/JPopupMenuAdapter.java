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

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

@SuppressWarnings("unchecked")
public class JPopupMenuAdapter extends CompositeAdapter {
	public JPopupMenuAdapter(){
		super(null);
	}
	@Override
	public Component cloneWidget() {
		JPopupMenu copy = (JPopupMenu) super.cloneWidget();
		JPopupMenu origin = (JPopupMenu) getWidget();
		int count = origin.getComponentCount();
		for(int i=0;i<count;i++){
			Component child = origin.getComponent(i);
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			copy.add(adapter.cloneWidget());
		}
		return copy;
	}

	@Override
	public String getBasename() {
		return "jPopupMenu";
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
		for(int i=0;i<count;i++){
			if(getChild(i)==child)
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
		JPopupMenu menu = new JPopupMenu(){
			private static final long serialVersionUID = 1L;
			@Override
			public void setVisible(boolean b) {
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
				super.setVisible(b);
			}
		};
		WidgetAdapter wa = ExtensionRegistry.createWidgetAdapter(JMenuItem.class);
		JMenuItem jmi = (JMenuItem)wa.getWidget();
		jmi.setText("menu item");		
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
		JPopupMenu jpopup=(JPopupMenu)getWidget();
		Component parent=jpopup.getInvoker();
		return (CompositeAdapter) WidgetAdapter.getWidgetAdapter(parent);
	}

	@Override
	public Class getWidgetClass() {
		return JPopupMenu.class;
	}
	
}

