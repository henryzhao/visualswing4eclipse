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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.ILayoutBean;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IProgressMonitor;

public class GridBagLayoutAdapter extends LayoutAdapter implements ILayoutBean {

	@Override
	public void initConainerLayout(Container container, IProgressMonitor monitor) {
		GridBagLayout layout = new GridBagLayout();
		layout.columnWeights = new double[]{0.1, 0.1, 0.1, 0.1};
		layout.columnWidths = new int[]{7, 7, 7, 7};
		layout.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1};
		layout.rowHeights = new int[]{7, 7, 7, 7};
		container.setLayout(new GridBagLayout());
	}

	@Override
	public boolean dragEnter(Point p) {
		return super.dragEnter(p);
	}

	@Override
	public boolean dragExit(Point p) {
		return super.dragExit(p);
	}

	@Override
	public boolean dragOver(Point p) {
		return super.dragOver(p);
	}

	@Override
	public boolean drop(Point p) {
		WidgetAdapter parent = WidgetAdapter.getWidgetAdapter(container);
		parent.clearAllSelected();
		for (WidgetAdapter todrop : parent.getDropWidget()) {
			container.add(todrop.getComponent(), new GridBagConstraints());
			todrop.setSelected(true);
		}
		parent.getRootAdapter().getWidget().validate();
		return super.drop(p);
	}

	@Override
	public boolean cloneLayout(JComponent panel) {
		GridBagLayout layout = (GridBagLayout) container.getLayout();
		panel.setLayout(copyLayout(panel));
		int count = container.getComponentCount();
		for (int i = 0; i < count; i++) {
			JComponent child = (JComponent) container.getComponent(i);
			WidgetAdapter cAdapter = WidgetAdapter.getWidgetAdapter(child);
			panel.add(cAdapter.cloneWidget(), layout.getConstraints(child));
		}
		return true;
	}

	@Override
	protected LayoutManager copyLayout(Container con) {
		GridBagLayout copy = new GridBagLayout();
		return copy;
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		container.add(child, constraints);
	}
	private static Stroke STROKE = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 2 }, 0);
	@Override
	public void paintGrid(Graphics g) {
		GridBagLayout layout = (GridBagLayout) container.getLayout();
		int[][] row_cols = layout.getLayoutDimensions();
		if (row_cols == null)
			return;
		g.setColor(new Color(0,100,180));
		Stroke old=((Graphics2D)g).getStroke();
		((Graphics2D)g).setStroke(STROKE);
		int w = container.getWidth();
		int h = container.getHeight();
		Point origin = layout.getLayoutOrigin();
		int[] widths = layout.columnWidths;
		if (widths == null)
			widths = row_cols[0];
		if (widths != null) {
			int x = origin!=null?origin.x:0;
			for (int i = 0; i < widths.length; i++) {
				g.drawLine(x, 0, x, h - 1);
				x += widths[i];
			}
			g.drawLine(x, 0, x, h - 1);
		}
		int[] heights = layout.rowHeights;
		if (heights == null)
			heights = row_cols[1];
		if (heights != null) {
			int y = origin!=null?origin.y:0;
			for (int i = 0; i < heights.length; i++) {
				g.drawLine(0, y, w - 1, y);
				y += heights[i];
			}
			g.drawLine(0, y, w - 1, y);
		}
		((Graphics2D)g).setStroke(old);		
		WidgetAdapter containerAdapter = WidgetAdapter.getWidgetAdapter(container);
		if (!containerAdapter.isSelected()) {
			g.setColor(Color.lightGray);
			g.drawRect(0, 0, w - 1, h - 1);
		}
	}
	@Override
	public void paintAnchor(Graphics g) {
		CompositeAdapter parentAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int count = parentAdapter.getChildCount();
		for(int i=0;i<count;i++){
			Component child = parentAdapter.getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			if(childAdapter.isSelected()){
				Rectangle rect=child.getBounds();
				g.setColor(Color.white);
				g.fillRect(rect.x+rect.width-18, rect.y-4, 10, 8);
				g.setColor(Color.black);
				g.drawRect(rect.x+rect.width-18, rect.y-4, 10, 8);
				Polygon p = new Polygon();
				p.addPoint(rect.x+rect.width-16, rect.y-1);
				p.addPoint(rect.x+rect.width-9, rect.y-1);
				p.addPoint(rect.x+rect.width-13, rect.y+3);
				g.setColor(Color.black);
				g.fillPolygon(p);
			}
		}	
	}
	@Override
	public Object getChildConstraints(Component child) {
		GridBagLayout layout = (GridBagLayout) container.getLayout();
		return layout.getConstraints(child);
	}
}
