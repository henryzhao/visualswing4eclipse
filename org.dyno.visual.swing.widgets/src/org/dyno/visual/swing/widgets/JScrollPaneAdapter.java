
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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JScrollPaneAdapter extends CompositeAdapter {
	private static Color RED_COLOR = new Color(255, 164, 0);
	private static Color GREEN_COLOR = new Color(164, 255, 0);
	private static Stroke STROKE;
	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 2 }, 0);
	}

	public JScrollPaneAdapter() {
		super(null);
	}

	@Override
	public boolean isViewContainer() {
		return true;
	}
	public boolean removeChild(Component child) {
		JScrollPane jsp = (JScrollPane)getWidget();
		Component view=jsp.getViewport().getView();
		if(child==view){
			jsp.setViewportView(null);
			return true;
		}else{
			return super.removeChild(child);
		}
	}
	@Override
	public Rectangle getVisibleRect(Component comp) {
		JComponent component = (JComponent)comp;
		return component.getVisibleRect();
	}

	protected Component createWidget() {
		JScrollPane jp = new JScrollPane();
		Dimension size = new Dimension(100, 100);
		jp.setSize(size);
		return jp;
	}

	@Override
	public boolean interceptPoint(Point p, int ad) {
		Component comp = getWidget();
		return p.x >= -ad && p.y >= -ad && p.x < comp.getWidth() + ad && p.y < comp.getHeight() + ad
				&& !(p.x >= ad && p.y >= ad && p.x < comp.getWidth() - ad && p.y < comp.getHeight() - ad);
	}

	@Override
	public Component getChild(int index) {
		JScrollPane jsp = (JScrollPane) getWidget();
		return (JComponent) jsp.getViewport().getView();
	}

	@Override
	public int getChildCount() {
		JScrollPane jsp = (JScrollPane) getWidget();
		Component comp = jsp.getViewport().getView();
		return comp == null ? 0 : 1;
	}

	@Override
	public int getIndexOfChild(Component child) {
		int size = getChildCount();
		for (int i = 0; i < size; i++) {
			Component comp = getChild(i);
			if (comp == child)
				return i;
		}
		return -1;
	}

	@Override
	public boolean allowChildResize() {
		return false;
	}

	@Override
	public boolean dragOver(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragOver(p);
		setMascotLocation(p);
		return true;
	}

	private boolean hovered;

	@Override
	public boolean dragEnter(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragEnter(p);
		setMascotLocation(p);
		hovered = true;
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragExit(p);
		setMascotLocation(p);
		hovered = false;
		return true;
	}

	@Override
	public boolean drop(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.drop(p);
		setMascotLocation(p);
		if (isPermitted()) {
			JScrollPane jsp = (JScrollPane) getWidget();
			WidgetAdapter todrop = getDropWidget().get(0);
			jsp.setViewportView(todrop.getWidget());
			todrop.requestNewName();
			getRootAdapter().getWidget().validate();
			clearAllSelected();
			todrop.setSelected(true);
		}
		hovered = false;
		return true;
	}

	@Override
	public void paintHovered(Graphics g) {
		if (hovered) {
			Graphics2D g2d = (Graphics2D) g;
			Stroke olds = g2d.getStroke();
			Composite oldc = g2d.getComposite();
			g2d.setColor(isPermitted() ? GREEN_COLOR : RED_COLOR);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			int w = getWidget().getWidth();
			int h = getWidget().getHeight();
			g2d.fillRect(0, 0, w, h);
			g2d.setColor(isPermitted() ? RED_COLOR : GREEN_COLOR);
			g2d.setStroke(STROKE);
			g2d.drawRect(0, 0, w - 1, h - 1);
			g2d.setComposite(oldc);
			g2d.setStroke(olds);
		}
	}

	private boolean isPermitted() {
		List<WidgetAdapter>adapters=getDropWidget();
		if(adapters.size()!=1)
			return false;		
		Component comp = ((JScrollPane) getWidget()).getViewport().getView();
		return comp == null;
	}

	@Override
	public boolean isEnclosingContainer() {
		return true;
	}

	@Override
	protected boolean isChildVisible(Component child) {
		JScrollPane jsp = (JScrollPane) getWidget();
		return child == jsp.getViewport().getView();
	}

	@Override
	public void showChild(Component widget) {
		widget.setVisible(true);
	}

	@Override
	public Component cloneWidget() {
		JScrollPane copy = (JScrollPane) super.cloneWidget();
		JScrollPane jsp = (JScrollPane) getWidget();
		JComponent child = (JComponent) jsp.getViewport().getView();
		if (child != null) {
			WidgetAdapter cAdapter = WidgetAdapter.getWidgetAdapter(child);
			copy.setViewportView(cAdapter.cloneWidget());
		}
		return copy;
	}


	@Override
	protected Component newWidget() {
		return new JScrollPane();
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		JScrollPane jsp = (JScrollPane) getWidget();
		jsp.setViewportView(child);
	}

	@Override
	public Object getChildConstraints(Component child) {
		return null;
	}
	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JScrollPane.class;
	}
	
}

