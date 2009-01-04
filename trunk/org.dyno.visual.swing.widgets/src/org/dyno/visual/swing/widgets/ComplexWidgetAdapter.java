
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
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public abstract class ComplexWidgetAdapter extends WidgetAdapter {

	public ComplexWidgetAdapter() {
	}

	public ComplexWidgetAdapter(String name) {
		super(name);
	}

	@Override
	public void requestNewName() {
		super.requestNewName();
		if (parent instanceof JScrollPane) {
			WidgetAdapter parentAdapter = WidgetAdapter.getWidgetAdapter(parent);
			if (parentAdapter != null)
				parentAdapter.requestNewName();
		} else {
			Component parentWidget = getWidget();
			while (parentWidget != null
					&& !(parentWidget instanceof JScrollPane)) {
				parentWidget = parentWidget.getParent();
			}
			if (parentWidget != null) {
				WidgetAdapter parentAdapter = WidgetAdapter
						.getWidgetAdapter(parentWidget);
				if (parentAdapter != null)
					parentAdapter.requestNewName();
			}
		}
	}

	@Override
	public Component getComponent() {
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
		paintComponent(g, (JComponent)getComponent());
	}

}

