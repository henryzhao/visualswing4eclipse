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
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

@SuppressWarnings("unchecked")
public class JCheckBoxMenuItemAdapter extends WidgetAdapter {
	public JCheckBoxMenuItemAdapter(){
		super(null);
	}

	@Override
	protected Component createWidget() {
		JCheckBoxMenuItem jmi = new JCheckBoxMenuItem();
		requestGlobalNewName();
		jmi.setText(getName());
		jmi.setSize(jmi.getPreferredSize());
		jmi.doLayout();
		return jmi;
	}
	public boolean isMoveable() {
		return true;
	}
	public boolean isResizable() {
		return false;
	}
	@Override
	protected Component newWidget() {
		return new JCheckBoxMenuItem();
	}
	public CompositeAdapter getParentAdapter() {
		Component me = getWidget();
		JPopupMenu jpm = (JPopupMenu) me.getParent();
		Component parent = jpm.getInvoker();
		return (CompositeAdapter) WidgetAdapter.getWidgetAdapter(parent);
	}
	@Override
	public Component cloneWidget() {
		JCheckBoxMenuItem jmi = (JCheckBoxMenuItem) super.cloneWidget();
		JCheckBoxMenuItem origin = (JCheckBoxMenuItem)getWidget();
		jmi.setText(origin.getText());
		jmi.setSelected(origin.isSelected());
		return jmi;
	}

	@Override
	public Class getWidgetClass() {
		return JCheckBoxMenuItem.class;
	}

	@Override
	public IAdapter getParent() {
		JCheckBoxMenuItem jb = (JCheckBoxMenuItem) getWidget();		
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

}

