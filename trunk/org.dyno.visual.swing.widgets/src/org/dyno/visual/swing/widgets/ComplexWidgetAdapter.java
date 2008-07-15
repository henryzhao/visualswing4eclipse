/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public abstract class ComplexWidgetAdapter extends WidgetAdapter {

	public ComplexWidgetAdapter() {
		super();
	}

	public ComplexWidgetAdapter(String name) {
		super(name);
	}

	@Override
	public JComponent getComponent() {
		if (parent == null) {
			WidgetAdapter jspa = ExtensionRegistry.createWidgetAdapter(JScrollPane.class);
			parent = ((JScrollPane) jspa.getWidget());
			parent.setViewportView(getWidget());
			parent.addNotify();
			parent.setSize(getWidget().getSize());			
			layoutContainer(parent);
			parent.validate();
			jspa.setHotspotPoint(new Point(parent.getWidth() / 2, parent.getHeight() / 2));
		}
		return parent;
	}

	private JScrollPane parent;

	@Override
	public void paintMascot(Graphics g) {
		paintComponent(g, getComponent());
	}

}
