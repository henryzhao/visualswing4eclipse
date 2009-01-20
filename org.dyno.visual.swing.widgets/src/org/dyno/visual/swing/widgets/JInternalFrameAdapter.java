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
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.RootPaneContainerAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JInternalFrameAdapter extends RootPaneContainerAdapter {
	private static JDesktopPane desktopPane = new JDesktopPane();
	private JPanel contentPane;
	private JPanelAdapter contentAdapter;

	public JInternalFrameAdapter() {
		super(null);
	}

	@Override
	public boolean allowChildResize(Component child) {
		if (child instanceof JMenuBar)
			return false;
		return contentAdapter.allowChildResize(child);
	}
	@Override
	public Component getRootPane() {
		return getWidget();
	}
	public void doLayout() {
		CompositeAdapter content = getContentAdapter();
		content.doLayout();
	}
	public boolean removeChild(Component child) {
		if (child instanceof JMenuBar) {
			JInternalFrame jframe = (JInternalFrame) getWidget();
			jframe.setJMenuBar(null);
			return true;
		} else
			return getContentAdapter().removeChild(child);
	}	
	@Override
	public void clearSelection() {
		setSelected(false);
		JInternalFrame jframe = (JInternalFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb != null) {
			WidgetAdapter jmbAdapter = WidgetAdapter.getWidgetAdapter(jmb);
			jmbAdapter.clearSelection();
		}
		getContentAdapter().clearSelection();
	}
	@Override
	public Component cloneWidget() {
		JInternalFrame copy = (JInternalFrame) super.cloneWidget();
		JInternalFrame jframe = (JInternalFrame) getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		if (jmb != null) {
			WidgetAdapter jmbAdapter = WidgetAdapter.getWidgetAdapter(jmb);
			JMenuBar copyjmb=(JMenuBar) jmbAdapter.cloneWidget();
			copy.setJMenuBar(copyjmb);
		}
		CompositeAdapter content = getContentAdapter();
		copy.setContentPane((JComponent) content.cloneWidget());
		return copy;
	}


	@Override
	public boolean needGenBoundCode() {
		Container panel = (Container) getContentPane();
		LayoutManager layout = panel.getLayout();
		return layout == null;
	}
	public CompositeAdapter getContentAdapter() {
		if (contentAdapter == null) {
			contentAdapter = (JPanelAdapter) ExtensionRegistry.createWidgetAdapter(JPanel.class);
			((JPanelAdapter) contentAdapter).setIntermediate(true);
			JInternalFrame jif = (JInternalFrame) getWidget();
			contentPane = (JPanel) jif.getContentPane();
			contentAdapter.setWidget(contentPane);
			contentAdapter.setDirty(false);
			contentAdapter.setName(getName());
			((JPanelAdapter)contentAdapter).setDelegate(this);
		}
		return contentAdapter;
	}

	@Override
	public boolean isEnclosingContainer() {
		return true;
	}

	@Override
	public boolean interceptPoint(Point p, int ad) {
		JInternalFrame comp = (JInternalFrame) getWidget();
		return p.x >= -ad
				&& p.y >= -ad
				&& p.x < comp.getWidth() + ad
				&& p.y < comp.getHeight() + ad
				&& !(p.x >= ad && p.y >= ad + TITLE_HEIGHT
						&& p.x < comp.getWidth() - ad && p.y < comp.getHeight()
						- ad);
	}

	private static int TITLE_HEIGHT = 22;

	@Override
	protected Component createWidget() {
		JInternalFrame jif = new JInternalFrame();		
		Dimension size = new Dimension(100, 100);
		jif.setLayout(new GroupLayout());
		jif.setSize(size);
		JavaUtil.layoutContainer(jif);
		jif.validate();
		jif.addNotify();
		desktopPane.add(jif);
		jif.setVisible(true);
		return jif;
	}


	@Override
	public Component getChild(int index) {
		JInternalFrame jif = (JInternalFrame) getWidget();
		JMenuBar jmb = jif.getJMenuBar();
		if (jmb == null)
			return getContentAdapter().getChild(index);
		else if (index == 0)
			return jmb;
		else
			return getContentAdapter().getChild(index - 1);
	}

	@Override
	public int getChildCount() {
		JInternalFrame jif = (JInternalFrame) getWidget();
		JMenuBar jmb=jif.getJMenuBar();		
		int count = getContentAdapter().getChildCount();
		return jmb==null?count:count+1;
	}

	@Override
	public int getIndexOfChild(Component child) {
		JInternalFrame jif = (JInternalFrame) getWidget();
		JMenuBar jmb = jif.getJMenuBar();
		if (jmb == null)
			return getContentAdapter().getIndexOfChild(child);
		else if (child == jmb)
			return 0;
		else
			return getContentAdapter().getIndexOfChild(child) + 1;
	}
	@Override
	protected Component newWidget() {
		return new JInternalFrame();
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		if(child instanceof JMenuBar){
			JInternalFrame jif = (JInternalFrame) getWidget();
			jif.setJMenuBar((JMenuBar)child);
		}else
			getContentAdapter().addChildByConstraints(child, constraints);
	}

	@Override
	public Object getChildConstraints(Component child) {
		if (child instanceof JMenuBar)
			return null;		
		return getContentAdapter().getChildConstraints(child);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JInternalFrame.class;
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
