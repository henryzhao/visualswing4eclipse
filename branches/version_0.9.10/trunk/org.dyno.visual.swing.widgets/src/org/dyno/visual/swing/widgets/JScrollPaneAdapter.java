
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
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

@SuppressWarnings("unchecked")
public class JScrollPaneAdapter extends CompositeAdapter {

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
	public boolean allowChildResize(Component child) {
		return false;
	}

	@Override
	public boolean isEnclosingContainer() {
		return true;
	}

	@Override
	public boolean isChildVisible(Component child) {
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
	public Class getWidgetClass() {
		return JScrollPane.class;
	}
	
}

