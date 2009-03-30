
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
import javax.swing.JToolBar;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

@SuppressWarnings("unchecked")
public class JToolBarAdapter extends CompositeAdapter {
	public JToolBarAdapter() {
		super(null);
	}

	@Override
	protected Component createWidget() {
		JToolBar toolBar = new JToolBar();
		Dimension size = new Dimension(100, 23);
		toolBar.setSize(size);
		toolBar.doLayout();
		toolBar.validate();
		return toolBar;
	}


	@Override
	public JComponent cloneWidget() {
		JToolBar copy = (JToolBar) super.cloneWidget();
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			Component child = getChild(i);
			WidgetAdapter cAdapter = WidgetAdapter.getWidgetAdapter(child);
			copy.add(cAdapter.cloneWidget());
		}
		return copy;
	}


	@Override
	protected JComponent newWidget() {
		return new JToolBar();
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		JToolBar toolbar = (JToolBar)getWidget();
		toolbar.add(child);
	}

	@Override
	public Object getChildConstraints(Component child) {
		return null;
	}	
	@Override
	public Class getWidgetClass() {
		return JToolBar.class;
	}
	
}

