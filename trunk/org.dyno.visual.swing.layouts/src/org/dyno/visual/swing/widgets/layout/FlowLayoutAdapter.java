
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
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.dyno.visual.swing.base.WidgetProperty;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IProgressMonitor;

public class FlowLayoutAdapter extends LayoutAdapter {
	private Thumb thumb;

	@Override
	public void paintHovered(Graphics g) {
		if (thumb != null) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(STROKE);
			g2d.setColor(RED_COLOR);
			g2d.drawRect(thumb.x - THUMB_PAD / 2, thumb.y - THUMB_PAD / 2, THUMB_PAD, THUMB_PAD);
		}
	}
	
	@Override
	public boolean dragEnter(Point p) {
		thumb = getClosetThumb(p);
		return true;
	}

	private Thumb getClosetThumb(Point hoverPoint) {
		int x = 0, y = 0;
		int w = container.getWidth();
		int size = container.getComponentCount();
		FlowLayout layout = (FlowLayout) container.getLayout();
		Insets insets = container.getInsets();
		if (size == 0) {
			x = w / 2;
			y = layout.getVgap() + THUMB_PAD / 2 + insets.top;
			return new Thumb(0, x, y);
		} else {
			double mind = Double.MAX_VALUE;
			int pi = 0;
			for (int i = 0; i < size; i++) {
				Component child = container.getComponent(i);
				Rectangle bounds = child.getBounds();
				int my = bounds.y + THUMB_PAD / 2;
				int mx = bounds.x - layout.getHgap() / 2;
				double currentd = hoverPoint.distance(mx, my);
				double delta = mind - currentd;
				if (delta > 0) {
					mind = currentd;
					x = mx;
					y = my;
					pi = i;
				}
			}
			Component child = container.getComponent(size - 1);
			Rectangle bounds = child.getBounds();
			int my = bounds.y + THUMB_PAD / 2;
			int mx = bounds.x + bounds.width - layout.getHgap() / 2;
			double currentd = hoverPoint.distance(mx, my);
			double delta = mind - currentd;
			if (delta > 0) {
				mind = currentd;
				x = mx;
				y = my;
				pi = size;
			}
			return new Thumb(pi, x, y);
		}
	}

	@Override
	public boolean dragExit(Point p) {
		thumb = null;
		return true;
	}

	@Override
	public boolean dragOver(Point p) {
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		parent.setMascotLocation(p);
		thumb = getClosetThumb(p);
		return true;
	}

	@Override
	public boolean drop(Point p) {
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter
				.getWidgetAdapter(container);
		thumb = getClosetThumb(p);
		parent.clearAllSelected();
		for (WidgetAdapter todrop : parent.getDropWidget()) {
			int size = container.getComponentCount();
			if (thumb.pi == size)
				container.add(todrop.getParentContainer());
			else
				container.add(todrop.getParentContainer(), thumb.pi);
			todrop.setSelected(true);
		}
		parent.getRootAdapter().getWidget().validate();
		thumb = null;
		return true;
	}

	@Override
	public void initConainerLayout(Container container, IProgressMonitor monitor) {
		container.setLayout(new FlowLayout());
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
		FlowLayout layout = (FlowLayout) container.getLayout();
		FlowLayout copy = new FlowLayout();
		copy.setAlignment(layout.getAlignment());
		copy.setAlignOnBaseline(layout.getAlignOnBaseline());
		copy.setHgap(layout.getHgap());
		copy.setVgap(layout.getVgap());
		return copy;
	}

	@Override
	protected IWidgetPropertyDescriptor[] getLayoutProperties() {
		WidgetProperty hgapProperty = new WidgetProperty("hgap","hgap", FlowLayout.class);
		WidgetProperty vgapProperty = new WidgetProperty("vgap", "vgap", FlowLayout.class);
		WidgetProperty alignOnBaselineProperty = new WidgetProperty("alignOnBaseline", "alignOnBaseline", FlowLayout.class);
		WidgetProperty alignmentProperty = new WidgetProperty("alignment", "alignment", FlowLayout.class, new FlowLayoutAlignmentRenderer(), new FlowLayoutAlignmentEditor());
		return new IWidgetPropertyDescriptor[]{alignmentProperty, hgapProperty, vgapProperty, alignOnBaselineProperty};
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

