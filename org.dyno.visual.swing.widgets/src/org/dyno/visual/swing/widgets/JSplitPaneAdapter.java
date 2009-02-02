
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

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

@SuppressWarnings("unchecked")
public class JSplitPaneAdapter extends CompositeAdapter {

	public JSplitPaneAdapter() {
		super(null);
	}

	@Override
	protected JComponent createWidget() {
		JSplitPane jsp = new JSplitPane();
		Dimension size = new Dimension(100, 100);
		jsp.setSize(size);
		jsp.doLayout();
		jsp.validate();
		return jsp;
	}

	private boolean existsAndDesigning(JComponent comp) {
		return comp != null && WidgetAdapter.getWidgetAdapter(comp) != null;
	}

	@Override
	public JComponent getChild(int index) {
		JSplitPane jtp = (JSplitPane) getWidget();
		JComponent comp = null;
		JComponent left, right;
		if (jtp.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
			left = (JComponent) jtp.getLeftComponent();
			right = (JComponent) jtp.getRightComponent();
		} else {
			left = (JComponent) jtp.getTopComponent();
			right = (JComponent) jtp.getBottomComponent();
		}
		if (existsAndDesigning(left)) {
			if (existsAndDesigning(right)) {
				if (index == 0)
					comp = left;
				else if (index == 1)
					comp = right;
			} else {
				if (index == 0)
					comp = left;
			}
		} else {
			if (existsAndDesigning(right)) {
				if (index == 0)
					comp = right;
			}
		}
		return (JComponent) comp;
	}

	@Override
	public int getChildCount() {
		JSplitPane jtp = (JSplitPane) getWidget();
		JComponent left, right;
		if (jtp.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
			left = (JComponent) jtp.getLeftComponent();
			right = (JComponent) jtp.getRightComponent();
		} else {
			left = (JComponent) jtp.getTopComponent();
			right = (JComponent) jtp.getBottomComponent();
		}
		return (existsAndDesigning(left) ? 1 : 0)
				+ (existsAndDesigning(right) ? 1 : 0);
	}

	@Override
	public int getIndexOfChild(Component child) {
		JSplitPane jtp = (JSplitPane) getWidget();
		JComponent left, right;
		if (jtp.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
			left = (JComponent) jtp.getLeftComponent();
			right = (JComponent) jtp.getRightComponent();
		} else {
			left = (JComponent) jtp.getTopComponent();
			right = (JComponent) jtp.getBottomComponent();
		}
		if (existsAndDesigning(left)) {
			if (existsAndDesigning(right)) {
				if (child == left)
					return 0;
				if (child == right)
					return 1;
				return -1;
			} else {
				if (child == left)
					return 0;
				return -1;
			}
		} else {
			if (existsAndDesigning(right)) {
				if (child == right)
					return 0;
				return -1;
			} else
				return -1;
		}
	}

	@Override
	public Component cloneWidget() {
		JSplitPane jsp = (JSplitPane) getWidget();
		JSplitPane copy = (JSplitPane) super.cloneWidget();
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			JComponent child = getChild(i);
			WidgetAdapter cAdapter = WidgetAdapter.getWidgetAdapter(child);
			Component cloneChild = cAdapter.cloneWidget();
			int orientation = jsp.getOrientation();
			if (orientation == JSplitPane.HORIZONTAL_SPLIT) {
				if (child == jsp.getLeftComponent()) {
					copy.setLeftComponent(cloneChild);
				} else if (child == jsp.getRightComponent()) {
					copy.setRightComponent(cloneChild);
				}
			} else {
				if (child == jsp.getTopComponent()) {
					copy.setTopComponent(cloneChild);
				} else if (child == jsp.getBottomComponent()) {
					copy.setBottomComponent(cloneChild);
				}
			}
		}
		return copy;
	}


	public boolean isEnclosingContainer() {
		return true;
	}

	@Override
	public boolean interceptPoint(Point p, int ad) {
		JSplitPane comp = (JSplitPane) getWidget();
		if (p.x >= -ad
				&& p.y >= -ad
				&& p.x < comp.getWidth() + ad
				&& p.y < comp.getHeight() + ad
				&& !(p.x >= ad && p.y >= ad && p.x < comp.getWidth() - ad && p.y < comp
						.getHeight()
						- ad))
			return true;
		int location = comp.getDividerLocation();
		int div = comp.getDividerSize();
		if (comp.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
			if (p.x >= location && p.x <= location + div)
				return true;
			else
				return false;
		} else {
			if (p.y >= location && p.y <= location + div)
				return true;
			else
				return false;
		}
	}

	@Override
	protected JComponent newWidget() {
		return new JSplitPane();
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		JSplitPane jsp = (JSplitPane) getWidget();
		if (jsp.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
			if ("left".equals(constraints)) //$NON-NLS-1$
				jsp.setLeftComponent(child);
			else if ("right".equals(constraints)) //$NON-NLS-1$
				jsp.setRightComponent(child);
		} else {
			if ("top".equals(constraints)) //$NON-NLS-1$
				jsp.setTopComponent(child);
			else if ("bottom".equals(constraints)) //$NON-NLS-1$
				jsp.setBottomComponent(child);
		}
	}

	@Override
	public Object getChildConstraints(Component child) {
		JSplitPane jsp = (JSplitPane) getWidget();
		if (jsp.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
			if (jsp.getLeftComponent() == child)
				return "left"; //$NON-NLS-1$
			else if (jsp.getRightComponent() == child)
				return "right"; //$NON-NLS-1$
		} else {
			if (jsp.getTopComponent() == child)
				return "top"; //$NON-NLS-1$
			else if (jsp.getBottomComponent() == child)
				return "bottom"; //$NON-NLS-1$
		}
		return null;
	}
	@Override
	public Class getWidgetClass() {
		return JSplitPane.class;
	}
}

