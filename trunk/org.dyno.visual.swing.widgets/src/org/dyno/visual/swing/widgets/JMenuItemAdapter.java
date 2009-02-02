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

import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

@SuppressWarnings("unchecked")
public class JMenuItemAdapter extends WidgetAdapter {
	public JMenuItemAdapter() {
		super(null);
	}

	@Override
	protected Component createWidget() {
		JMenuItem jmi = new JMenuItem();
		requestGlobalNewName();		
		jmi.setText(getName());
		jmi.setSize(jmi.getPreferredSize());
		jmi.doLayout();
		return jmi;
	}

	public CompositeAdapter getParentAdapter() {
		Component me = getWidget();
		JPopupMenu jpm = (JPopupMenu) me.getParent();
		Component parent = jpm.getInvoker();
		return (CompositeAdapter) WidgetAdapter.getWidgetAdapter(parent);
	}

	public boolean isMoveable() {
		return true;
	}
	public boolean isResizable() {
		return false;
	}
	@Override
	protected Component newWidget() {
		JMenuItem jmi = new JMenuItem();
		return jmi;
	}
	@Override
	public Component cloneWidget() {
		JMenuItem jmi = (JMenuItem) super.cloneWidget();
		JMenuItem origin = (JMenuItem) getWidget();
		jmi.setText(origin.getText());
		return jmi;
	}
	@Override
	public Class getWidgetClass() {
		return JMenuItem.class;
	}
	@Override
	public IAdapter getParent() {
		JMenuItem jb = (JMenuItem) getWidget();		
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
		JMenuItem jb = (JMenuItem) getWidget();
		DefaultButtonModel dbm = (DefaultButtonModel) jb.getModel();
		ButtonGroup bg = dbm.getGroup();
		if(bg!=null){
			bg.remove(jb);
		}
	}
	
}

