
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
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JTabbedPaneAdapter extends CompositeAdapter {
	public JTabbedPaneAdapter() {
		super(null);
	}

	@Override
	protected Component createWidget() {
		JTabbedPane tab = new JTabbedPane();
		Dimension size = new Dimension(100, 100);
		tab.setSize(size);
		tab.doLayout();
		tab.validate();
		return tab;
	}


	@Override
	public boolean allowChildResize(Component child) {
		return false;
	}

	@Override
	public Component getChild(int index) {
		JTabbedPane tp = (JTabbedPane) getWidget();
		return (JComponent) tp.getComponentAt(index);
	}

	@Override
	public int getChildCount() {
		JTabbedPane tp = (JTabbedPane) getWidget();
		return tp.getComponentCount();
	}

	@Override
	public int getIndexOfChild(Component child) {
		JTabbedPane tp = (JTabbedPane) getWidget();
		return tp.indexOfComponent(child);
	}

	@Override
	public boolean isChildVisible(Component child) {
		JTabbedPane tp = (JTabbedPane) getWidget();
		return child == tp.getSelectedComponent();
	}

	@Override
	public void showChild(Component widget) {
		JTabbedPane tp = (JTabbedPane) getWidget();
		tp.setSelectedComponent(widget);
	}

	@Override
	public JComponent cloneWidget() {
		JTabbedPane tp = (JTabbedPane) getWidget();
		JTabbedPane copy = (JTabbedPane) super.cloneWidget();
		int count = tp.getTabCount();
		for (int i = 0; i < count; i++) {
			JComponent child = (JComponent) tp.getComponentAt(i);
			WidgetAdapter cAdapter = WidgetAdapter.getWidgetAdapter(child);
			copy.addTab(tp.getTitleAt(i), cAdapter.cloneWidget());
		}
		return copy;
	}


	@Override
	protected JComponent newWidget() {
		return new JTabbedPane();
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		if (constraints != null) {
			JTabbedPane jtp = (JTabbedPane) getWidget();
			jtp.addTab((String) constraints, child);
		}
	}

	@Override
	public Object getChildConstraints(Component child) {
		JTabbedPane jtp = (JTabbedPane) getWidget();
		int index = jtp.indexOfComponent(child);
		if (index != -1)
			return jtp.getTitleAt(index);
		else
			return null;
	}
	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JTabbedPane.class;
	}
}

