
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

package org.dyno.visual.swing.widgets.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.dyno.visual.swing.base.WidgetProperty;
import org.dyno.visual.swing.plugin.spi.ILayoutBean;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IProgressMonitor;

public class GridLayoutAdapter extends LayoutAdapter implements ILayoutBean {
	private Rectangle placement;
	private int index;

	@Override
	public boolean dragExit(Point p) {
		placement = null;
		return true;
	}

	private boolean drag(Point p) {
		Insets insets = container.getInsets();
		int width = container.getWidth();
		int height = container.getHeight();
		WidgetAdapter parent = WidgetAdapter.getWidgetAdapter(container);
		if (container.getComponentCount() == 0) {
			placement = new Rectangle(insets.left, insets.top, width - insets.left - insets.right, height - insets.top - insets.bottom);
			index = -1;
		} else {
			Component comp = container.getComponent(0);
			int w = comp.getWidth();
			int h = comp.getHeight();
			int n = width / w;
			int xi = p.x / w;
			int yi = p.y / h;
			int i = yi * n + xi;
			int count = container.getComponentCount();
			if (i >= count) {
				yi = count / n;
				xi = count % n;
				index = -1;
			} else {
				index = i;
			}
			int x = xi * w - THUMB_PAD / 2;
			int y = yi * h + THUMB_PAD / 2;
			int thumb_width = THUMB_PAD;
			int thumb_height = h - THUMB_PAD;
			placement = new Rectangle(x + insets.left, y + insets.top, thumb_width, thumb_height);
		}
		parent.setMascotLocation(p);
		return true;
	}

	@Override
	public boolean dragEnter(Point p) {
		return drag(p);
	}

	@Override
	public boolean dragOver(Point p) {
		return drag(p);
	}

	@Override
	public boolean drop(Point p) {
		drag(p);
		WidgetAdapter parent = WidgetAdapter.getWidgetAdapter(container);
		parent.clearAllSelected();
		for (WidgetAdapter todrop : parent.getDropWidget()) {
			if (index != -1)
				container.add(todrop.getParentContainer(), index);
			else
				container.add(todrop.getParentContainer());
			todrop.setSelected(true);
		}
		parent.getRootAdapter().getWidget().validate();
		placement = null;
		return true;
	}

	@Override
	public void paintHovered(Graphics g) {
		if (placement != null) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(STROKE);
			g2d.setColor(RED_COLOR);
			g2d.drawRect(placement.x, placement.y, placement.width, placement.height);
		}
	}
	public void initConainerLayout(Container container, IProgressMonitor monitor) {
		GridLayout layout = new GridLayout(2, 2);
		container.setLayout(layout);
	}

	@Override
	public boolean cloneLayout(JComponent panel) {
		panel.setLayout(copyLayout(panel));
		int count = container.getComponentCount();
		for (int i = 0; i < count; i++) {
			JComponent child = (JComponent) container.getComponent(i);
			WidgetAdapter cAdapter = WidgetAdapter.getWidgetAdapter(child);
			panel.add(cAdapter.cloneWidget());
		}
		return true;
	}

	@Override
	protected LayoutManager copyLayout(Container con) {
		GridLayout layout = (GridLayout) container.getLayout();
		GridLayout copy = new GridLayout(layout.getRows(), layout.getColumns());
		copy.setHgap(layout.getHgap());
		copy.setVgap(layout.getVgap());
		return copy;
	}

	@Override
	protected IWidgetPropertyDescriptor[] getLayoutProperties() {
		WidgetProperty rowsProperty = new WidgetProperty("rows", GridLayout.class, 1);
		WidgetProperty columnsProperty = new WidgetProperty("columns", GridLayout.class, 0);
		WidgetProperty hgapProperty = new WidgetProperty("hgap", GridLayout.class, 0);
		WidgetProperty vgapProperty = new WidgetProperty("vgap", GridLayout.class, 0);
		return new IWidgetPropertyDescriptor[]{rowsProperty, columnsProperty, hgapProperty, vgapProperty};
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		container.add(child);
	}

	@Override
	public Object getChildConstraints(Component child) {
		return null;
	}
	
}

