
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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public abstract class ComplexWidgetAdapter extends WidgetAdapter {

	public ComplexWidgetAdapter() {
	}

	public ComplexWidgetAdapter(String name) {
		super(name);
	}

	@Override
	public void setWidget(Component widget) {
		super.setWidget(widget);
		if(parent==null){
			Container container=widget.getParent();
			if(container!=null&&container instanceof JViewport){
				container = container.getParent();
				if(container!=null&&container instanceof JScrollPane){
					parent = (JScrollPane) container;
				}
			}
		}
	}

	@Override
	public void requestNewName() {
		super.requestNewName();
		if (parent instanceof JScrollPane) {
			WidgetAdapter parentAdapter = WidgetAdapter.getWidgetAdapter(parent);
			if (parentAdapter != null&&parentAdapter.getName()==null)
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
				if (parentAdapter != null&&parentAdapter.getName()==null)
					parentAdapter.requestNewName();
			}
		}
	}

	@Override
	public Point getHotspotPoint() {
		Point p= super.getHotspotPoint();
		Component parent = getParentContainer();
		p=SwingUtilities.convertPoint(getWidget(), p, parent);
		return p;
	}

	public Component getParentContainer() {
		if (parent == null) {
			WidgetAdapter jspa = ExtensionRegistry.createWidgetAdapter(JScrollPane.class);
			parent = ((JScrollPane) jspa.getWidget());
			parent.setViewportView(getWidget());
			parent.addNotify();
			parent.setSize(getInitialSize());			
			JavaUtil.layoutContainer(parent);
			parent.validate();
			jspa.setHotspotPoint(new Point(parent.getWidth() / 2, parent.getHeight() / 2));
		}
		return parent;
	}
	protected abstract Dimension getInitialSize();
	private JScrollPane parent;

	@Override
	public void paintMascot(Graphics g) {
		paintComponent(g);
	}

}

