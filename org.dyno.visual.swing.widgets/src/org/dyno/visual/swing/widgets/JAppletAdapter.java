
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
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.RootPaneContainerAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.operations.IUndoableOperation;

public class JAppletAdapter extends RootPaneContainerAdapter {
	private JPanelAdapter contentAdapter;
	private JComponent contentPane;
	private JRootPane jrootPane;
	public JAppletAdapter() {
		super(null);
		createContentAdapter();
	}

	@Override
	public void setWidget(Component widget) {
		super.setWidget(widget);
		createContentAdapter();
	}

	private void createContentAdapter() {
		contentAdapter = (JPanelAdapter) ExtensionRegistry.createWidgetAdapter(JPanel.class);
		contentAdapter.setDelegate(this);
		JApplet me = (JApplet) getWidget();
		JavaUtil.layoutContainer(me);
		contentPane = (JComponent) me.getContentPane();
		jrootPane = me.getRootPane();
		contentAdapter.setWidget(contentPane);
		contentAdapter.setDelegate(this);
	}

	public void doLayout() {
		contentAdapter.doLayout();
	}


	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		if (child instanceof JMenuBar) {
			JApplet japplet = (JApplet) getWidget();
			japplet.setJMenuBar((JMenuBar) child);
		} else
			contentAdapter.addChildByConstraints(child, constraints);
	}

	@Override
	public void clearSelection() {
		setSelected(false);
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		if (jmb != null) {
			WidgetAdapter jmbAdapter = WidgetAdapter.getWidgetAdapter(jmb);
			jmbAdapter.clearSelection();
		}
		contentAdapter.clearSelection();
	}

	@Override
	protected Component createWidget() {
		return new JApplet();
	}

	@Override
	public Object getChildConstraints(Component child) {
		if (child instanceof JMenuBar)
			return null;
		return contentAdapter.getChildConstraints(child);
	}

	@Override
	public boolean isRoot() {
		return true;
	}

	@Override
	public Component getRootPane() {
		return jrootPane;
	}

	@Override
	public Rectangle getDesignBounds() {
		Rectangle bounds = this.jrootPane.getBounds();
		if (bounds.width <= 0)
			bounds.width = 400;
		if (bounds.height <= 0)
			bounds.height = 300;
		bounds.y = 24;
		bounds.x = 24;
		return bounds;
	}

	@Override
	protected Component newWidget() {
		return new JApplet();
	}

	@Override
	public Component cloneWidget() {
		JRootPane jrp = new JRootPane();
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		if (jmb != null) {
			WidgetAdapter jmbAdapter = WidgetAdapter.getWidgetAdapter(jmb);
			JMenuBar jmenubar = (JMenuBar) jmbAdapter.cloneWidget();
			jrp.setJMenuBar(jmenubar);
		}
		Container container = (Container) contentAdapter.cloneWidget();
		jrp.setContentPane(container);
		return jrp;
	}

	public Component getParentContainer() {
		return contentAdapter.getWidget();
	}

	@Override
	public void addAfter(Component hovering, Component dragged) {
		contentAdapter.addAfter(hovering, dragged);
	}

	@Override
	public void addBefore(Component hovering, Component dragged) {
		contentAdapter.addBefore(hovering, dragged);
	}

	@Override
	public void addChild(Component widget) {
		contentAdapter.addChild(widget);
	}

	@Override
	public boolean doAlignment(String id) {
		return contentAdapter.doAlignment(id);
	}

	@Override
	public IUndoableOperation doKeyPressed(KeyEvent e) {
		return contentAdapter.doKeyPressed(e);
	}

	@Override
	public Component getChild(int index) {
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		if (jmb == null)
			return contentAdapter.getChild(index);
		else if (index == 0)
			return jmb;
		else
			return contentAdapter.getChild(index - 1);
	}

	@Override
	public int getChildCount() {
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		int count = contentAdapter.getChildCount();
		return jmb == null ? count : (count + 1);
	}

	public String toString() {
		if (isRoot()) {
			return "[" + getWidgetName() + "]";
		} else {
			return getName() + " [" + getWidgetName() + "]";
		}
	}

	@Override
	public int getIndexOfChild(Component child) {
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		if (jmb == null)
			return contentAdapter.getIndexOfChild(child);
		else if (jmb == child)
			return 0;
		else
			return contentAdapter.getIndexOfChild(child) + 1;
	}

	@Override
	public boolean allowChildResize() {
		return contentAdapter.allowChildResize();
	}


	public Point convertToGlobal(Point p) {
		JApplet japplet = (JApplet) getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		if (jmb == null)
			return contentAdapter.convertToGlobal(p);
		else {
			p = SwingUtilities.convertPoint(jrootPane, p, contentPane);
			return contentAdapter.convertToGlobal(p);
		}
	}

	public Point convertToLocal(Point p) {
		return contentAdapter.convertToLocal(p);
	}

	public WidgetAdapter getRootAdapter() {
		return this;
	}

	public boolean removeChild(Component child) {
		if (child instanceof JMenuBar) {
			JApplet japplet = (JApplet) getWidget();
			japplet.setJMenuBar(null);
			return true;
		} else
			return contentAdapter.removeChild(child);
	}
	public WidgetAdapter getContentAdapter(){
		return contentAdapter;
	}
	@Override
	public boolean isChildVisible(Component child) {
		return contentAdapter.isChildVisible(child);
	}

	@Override
	public void showChild(Component widget) {
		contentAdapter.showChild(widget);
	}

	@Override
	public void adjustLayout(Component widget) {
		contentAdapter.adjustLayout(widget);
	}

	@Override
	public boolean isSelectionAlignResize(String id) {
		return contentAdapter.isSelectionAlignResize(id);
	}


	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JApplet.class;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapterClass) {
		Object adaptable = super.getAdapter(adapterClass);
		if(adaptable==null&&adapterClass==MouseInputListener.class){
			LayoutAdapter adapter=contentAdapter.getLayoutAdapter();
			if(adapter!=null)
				return adapter.getAdapter(adapterClass);
			else
				return null;
		}else
			return adaptable;
		
	}
}

