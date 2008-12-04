
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Stroke;

import javax.swing.JComponent;
import javax.swing.JToolBar;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JToolBarAdapter extends CompositeAdapter {
	private static int VAR_INDEX = 0;
	protected static Color RED_COLOR = new Color(255, 164, 0);
	protected static Stroke STROKE;
	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 2 }, 0);
	}
	private boolean hovered;

	public JToolBarAdapter() {
		super("jToolBar" + (VAR_INDEX++));
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
	public boolean dragEnter(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragEnter(p);
		hovered = true;
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragExit(p);
		hovered = false;
		return true;
	}

	@Override
	public boolean dragOver(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.dragOver(p);
		setMascotLocation(p);
		return true;
	}

	@Override
	public boolean drop(Point p) {
		if(isDroppingMenuItem()||isDroppingMenuBar())
			return super.drop(p);
		JToolBar toolbar = (JToolBar) getWidget();
		clearAllSelected();
		for (WidgetAdapter wa : getDropWidget()) {
			Component child = wa.getWidget();
			toolbar.add(child);
			wa.setSelected(true);
		}
		getWidget().validate();
		hovered = false;
		return true;
	}

	@Override
	public void paintFocused(Graphics g) {
		if (hovered&&getDropWidget().size()>0) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(STROKE);
			g2d.setColor(RED_COLOR);
			JToolBar parent = (JToolBar) getWidget();
			int size = parent.getComponentCount();
			Insets insets = parent.getInsets();
			int x = insets.left;
			int y = insets.top;
			Component drop = getDropWidget().get(0).getWidget();
			int w = drop.getWidth();
			int h = drop.getHeight();
			if (parent.getOrientation() == JToolBar.HORIZONTAL) {
				if (size > 0) {
					Component last = parent.getComponent(size - 1);
					x = last.getX() + last.getWidth();
					y = last.getY() + (last.getHeight() - drop.getHeight()) / 2;
				} else {
					x = insets.left;
					y = (parent.getHeight() - h - insets.top - insets.bottom) / 2 + insets.top;
				}
			} else {
				if (size > 0) {
					Component last = parent.getComponent(size - 1);
					y = last.getY() + last.getHeight();
					x = last.getX() + (last.getWidth() - drop.getWidth()) / 2;
				} else {
					y = insets.top;
					x = (parent.getWidth() - w - insets.left - insets.right) / 2 + insets.left;
				}
			}
			g2d.drawRect(x, y, w, h);
		}
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
	protected String createGetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.createGetCode(imports));
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			Component child = getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			String getMethodName = childAdapter.getCreationMethodName();			
			builder.append(getFieldName(getName()) + ".add(");
			builder.append(getMethodName + "());\n");
		}
		return builder.toString();
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

