
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

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JTabbedPaneAdapter extends CompositeAdapter {
	protected static Color RED_COLOR = new Color(255, 164, 0);
	protected static Color GREEN_COLOR = new Color(164, 255, 0);
	protected static Stroke STROKE;
	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 2 }, 0);
	}

	public JTabbedPaneAdapter() {
		super(null);
	}

	@Override
	protected Component createWidget() {
		JTabbedPane tab = new JTabbedPane();
		Dimension size = new Dimension(100, 100);
		tab.setSize(size);
		tab.doLayout();
		tab.validate();
		return tab;
	}

	@Override
	public void paintHovered(Graphics clipg) {
		if (hovered) {
			int w = getWidget().getWidth();
			int h = getWidget().getHeight();
			Graphics2D g2d = (Graphics2D) clipg;
			g2d.setColor(GREEN_COLOR);
			Composite oldComposite = g2d.getComposite();
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
			g2d.setComposite(composite);
			g2d.fillRect(0, 0, w, h);
			Stroke oldStroke = g2d.getStroke();
			g2d.setColor(RED_COLOR);
			g2d.setStroke(STROKE);
			g2d.drawRect(0, 0, w, h);
			g2d.setStroke(oldStroke);
			g2d.setComposite(oldComposite);
		}
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
		clearAllSelected();
		for (WidgetAdapter adapter : getDropWidget()) {
			Component child = adapter.getParentContainer();
			JTabbedPane jtp = (JTabbedPane) getWidget();
			if(adapter.getName()==null)
				adapter.setName(getNamespace().nextName(adapter.getBasename()));
			jtp.addTab(adapter.getName(), child);
			jtp.setSelectedComponent(child);
			adapter.setSelected(true);
			adapter.requestNewName();
		}
		hovered = false;
		return true;
	}

	private boolean hovered;
	private IEditor iEditor;

	@Override
	public IEditor getEditorAt(int x, int y) {
		if (iEditor == null) {
			iEditor = new LabelEditor();
		}
		return iEditor;
	}
	@Override
	public Object getWidgetValue() {
		JTabbedPane jtp = (JTabbedPane) getWidget();
		int index = jtp.getSelectedIndex();
		if (index != -1) {
			return jtp.getTitleAt(index);
		} else {
			return null;
		}
	}

	@Override
	public void setWidgetValue(Object value) {
		JTabbedPane jtp = (JTabbedPane) getWidget();
		int index = jtp.getSelectedIndex();
		if (index != -1) {
			jtp.setTitleAt(index, (String) value);
		}
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		JTabbedPane jtp = (JTabbedPane) getWidget();
		int index = getTabIndexAt(jtp, x, y);
		if (index != -1) {
			return jtp.getBoundsAt(index);
		}
		return null;
	}

	private int getTabIndexAt(JTabbedPane jtp, int x, int y) {
		int count = jtp.getTabCount();
		for (int i = 0; i < count; i++) {
			Rectangle rect = jtp.getBoundsAt(i);
			if ((rect != null) && rect.contains(x, y)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean allowChildResize() {
		return false;
	}

	@Override
	public Component getChild(int index) {
		JTabbedPane tp = (JTabbedPane) getWidget();
		return (JComponent) tp.getComponentAt(index);
	}

	@Override
	public int getChildCount() {
		JTabbedPane tp = (JTabbedPane) getWidget();
		return tp.getComponentCount();
	}

	@Override
	public int getIndexOfChild(Component child) {
		JTabbedPane tp = (JTabbedPane) getWidget();
		return tp.indexOfComponent(child);
	}

	@Override
	protected boolean isChildVisible(Component child) {
		JTabbedPane tp = (JTabbedPane) getWidget();
		return child == tp.getSelectedComponent();
	}

	@Override
	public void showChild(Component widget) {
		JTabbedPane tp = (JTabbedPane) getWidget();
		tp.setSelectedComponent(widget);
	}

	@Override
	public JComponent cloneWidget() {
		JTabbedPane tp = (JTabbedPane) getWidget();
		JTabbedPane copy = (JTabbedPane) super.cloneWidget();
		int count = tp.getTabCount();
		for (int i = 0; i < count; i++) {
			JComponent child = (JComponent) tp.getComponentAt(i);
			WidgetAdapter cAdapter = WidgetAdapter.getWidgetAdapter(child);
			copy.addTab(tp.getTitleAt(i), cAdapter.cloneWidget());
		}
		return copy;
	}


	@Override
	protected JComponent newWidget() {
		return new JTabbedPane();
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		if (constraints != null) {
			JTabbedPane jtp = (JTabbedPane) getWidget();
			jtp.addTab((String) constraints, child);
		}
	}

	@Override
	public Object getChildConstraints(Component child) {
		JTabbedPane jtp = (JTabbedPane) getWidget();
		int index = jtp.indexOfComponent(child);
		if (index != -1)
			return jtp.getTitleAt(index);
		else
			return null;
	}
	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JTabbedPane.class;
	}
}

