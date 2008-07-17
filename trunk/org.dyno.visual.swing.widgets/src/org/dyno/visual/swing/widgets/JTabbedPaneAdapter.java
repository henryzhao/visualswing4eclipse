/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
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
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JTabbedPaneAdapter extends CompositeAdapter {
	private static int VAR_INDEX = 0;
	protected static Color RED_COLOR = new Color(255, 164, 0);
	protected static Color GREEN_COLOR = new Color(164, 255, 0);
	protected static Stroke STROKE;
	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 2 }, 0);
	}

	public JTabbedPaneAdapter() {
		super("jTabbedPane" + (VAR_INDEX++));
	}

	@Override
	protected JComponent createWidget() {
		JTabbedPane tab = new JTabbedPane();
		Dimension size = new Dimension(100, 100);
		tab.setSize(size);
		tab.doLayout();
		tab.validate();
		return tab;
	}

	@Override
	public void paintFocused(Graphics clipg) {
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
		hovered = true;
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		hovered = false;
		return true;
	}

	@Override
	public boolean dragOver(Point p) {
		setMascotLocation(p);
		return true;
	}

	@Override
	public boolean drop(Point p) {
		WidgetAdapter adapter = getDropWidget();
		JComponent child = adapter.getComponent();
		JTabbedPane jtp = (JTabbedPane) getWidget();
		jtp.addTab(adapter.getName(), child);
		jtp.setSelectedComponent(child);
		clearAllSelected();
		adapter.setSelected(true);
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
	public boolean widgetPressed(int x, int y) {
		JTabbedPane tabbedPane = (JTabbedPane) getWidget();
		int count = tabbedPane.getTabCount();
		for (int i = 0; i < count; i++) {
			Rectangle tabBounds = tabbedPane.getBoundsAt(i);
			if ((tabBounds != null) && tabBounds.contains(x, y)) {
				tabbedPane.setSelectedIndex(i);
				JComponent widget = (JComponent) tabbedPane.getComponent(i);
				clearAllSelected();
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(widget);
				adapter.setSelected(true);
				return false;
			}
		}
		return true;
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
	public JComponent getChild(int index) {
		JTabbedPane tp = (JTabbedPane) getWidget();
		return (JComponent) tp.getComponentAt(index);
	}

	@Override
	public int getChildCount() {
		JTabbedPane tp = (JTabbedPane) getWidget();
		return tp.getComponentCount();
	}

	@Override
	public int getIndexOfChild(JComponent child) {
		JTabbedPane tp = (JTabbedPane) getWidget();
		return tp.indexOfComponent(child);
	}

	@Override
	protected boolean isChildVisible(JComponent child) {
		JTabbedPane tp = (JTabbedPane) getWidget();
		return child == tp.getSelectedComponent();
	}

	@Override
	public void showChild(JComponent widget) {
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
	protected String createGetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.createGetCode(imports));
		JTabbedPane jtp = (JTabbedPane) getWidget();
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			JComponent child = getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			String getMethodName = getGetMethodName(childAdapter.getName());
			builder.append(getFieldName(getName()) + ".addTab(");
			String title = jtp.getTitleAt(i);
			builder.append("\"" + title + "\", ");
			builder.append(getMethodName + "());\n");
		}
		return builder.toString();
	}

	@Override
	protected JComponent newWidget() {
		return new JTabbedPane();
	}

	@Override
	public void addChildByConstraints(JComponent child, Object constraints) {
		if (constraints != null) {
			JTabbedPane jtp = (JTabbedPane) getWidget();
			jtp.addTab((String) constraints, child);
		}
	}

	@Override
	public Object getChildConstraints(JComponent child) {
		JTabbedPane jtp = (JTabbedPane) getWidget();
		int index = jtp.indexOfComponent(child);
		if (index != -1)
			return jtp.getTitleAt(index);
		else
			return null;
	}
}
