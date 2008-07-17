/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BoxLayout;
import javax.swing.JComponent;

import org.dyno.visual.swing.base.FieldProperty;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.ILayoutBean;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class BoxLayoutAdapter extends LayoutAdapter implements ILayoutBean {
	private Thumb thumb;

	@Override
	public void initConainerLayout(Container parent) {
		BoxLayout boxLayout = new BoxLayout(parent, BoxLayout.LINE_AXIS);
		parent.setLayout(boxLayout);
	}

	private boolean isHorizontal() {
		BoxLayout layout = (BoxLayout) container.getLayout();
		int axis = layout.getAxis();
		return axis == BoxLayout.LINE_AXIS || axis == BoxLayout.X_AXIS;
	}

	private Thumb getClosetThumb(Point hoverPoint) {
		boolean horizontal = isHorizontal();
		int x = 0, y = 0;
		int w = container.getWidth();
		int h = container.getHeight();
		int size = container.getComponentCount();
		if (size == 0) {
			if (horizontal) {
				x = THUMB_PAD / 2;
				y = h / 2;
			} else {
				x = w / 2;
				y = THUMB_PAD / 2;
			}
			return new Thumb(0, x, y);
		} else {
			double mind = Double.MAX_VALUE;
			int pi = 0;
			for (int i = 0; i < size; i++) {
				Component child = container.getComponent(i);
				Rectangle bounds = child.getBounds();
				int my = bounds.y + THUMB_PAD / 2;
				if (!horizontal)
					my = bounds.y;
				int mx = bounds.x;
				if (!horizontal)
					mx = bounds.x + bounds.width / 2;
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
			if (!horizontal)
				my = bounds.y + bounds.height;
			int mx = bounds.x + bounds.width;
			if (!horizontal)
				mx = bounds.x + bounds.width / 2;
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
	public boolean dragEnter(Point p) {
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter
				.getWidgetAdapter(container);
		parent.setMascotLocation(p);
		thumb = getClosetThumb(p);
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		thumb = null;
		return true;
	}

	@Override
	public boolean dragOver(Point p) {
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter
				.getWidgetAdapter(container);
		parent.setMascotLocation(p);
		thumb = getClosetThumb(p);
		return true;
	}

	@Override
	public boolean drop(Point p) {
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter
				.getWidgetAdapter(container);
		thumb = getClosetThumb(p);
		WidgetAdapter todrop = parent.getDropWidget();
		int size = container.getComponentCount();
		if (thumb.pi == size)
			container.add(todrop.getComponent());
		else
			container.add(todrop.getComponent(), thumb.pi);
		parent.getRootAdapter().getWidget().validate();
		parent.clearAllSelected();
		todrop.setSelected(true);
		thumb = null;
		return true;
	}

	@Override
	public void paintFocused(Graphics g) {
		if (thumb != null) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(STROKE);
			g2d.setColor(RED_COLOR);
			g2d.drawRect(thumb.x - THUMB_PAD / 2, thumb.y - THUMB_PAD / 2,
					THUMB_PAD, THUMB_PAD);
		}
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
		BoxLayout layout = (BoxLayout) container.getLayout();
		int axis = layout.getAxis();
		return new BoxLayout(con, axis);
	}

	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		BoxLayout layout = (BoxLayout) container.getLayout();
		int axis = layout.getAxis();
		String name=imports.addImport("javax.swing.BoxLayout");
		String strAxis = "X_AXIS";
		switch (axis) {
		case BoxLayout.X_AXIS:
			strAxis = "X_AXIS";
			break;
		case BoxLayout.Y_AXIS:
			strAxis = "Y_AXIS";
			break;
		case BoxLayout.LINE_AXIS:
			strAxis = "LINE_AXIS";
			break;
		case BoxLayout.PAGE_AXIS:
			strAxis = "PAGE_AXIS";
			break;
		}
		strAxis = name+"."+strAxis;
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(container);	
		String conName=adapter.isRoot()?"this":getFieldName(adapter.getName());
		return "new "+name+"("+conName+", "+strAxis+")";
	}

	@Override
	protected IWidgetPropertyDescriptor[] getLayoutProperties() {
		FieldProperty axisProperty = new FieldProperty("axis", "axis", BoxLayout.class, new BoxLayoutAxisRenderer(), new BoxLayoutAxisEditor());
		return new IWidgetPropertyDescriptor[]{axisProperty};
	}

	@Override
	public void addChildByConstraints(JComponent child, Object constraints) {
		container.add(child);
	}

	@Override
	public Object getChildConstraints(JComponent child) {
		return null;
	}
	
}
