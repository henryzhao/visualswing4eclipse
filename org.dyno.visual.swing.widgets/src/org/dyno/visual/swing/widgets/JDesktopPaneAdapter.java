
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
import java.awt.Rectangle;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JDesktopPaneAdapter extends CompositeAdapter {

	public JDesktopPaneAdapter() {
		super(null);
	}

	public boolean needGenBoundCode() {
		return true;
	}

	@Override
	protected Component createWidget() {
		JDesktopPane pane = new JDesktopPane();
		Dimension size = new Dimension(100, 100);
		pane.setSize(size);
		return pane;
	}

	@Override
	public boolean allowChildResize(Component child) {
		return true;
	}

	@Override
	public Component getChild(int index) {
		JDesktopPane jdp = (JDesktopPane) getWidget();
		JInternalFrame[] frames = jdp.getAllFrames();
		return frames[index];
	}

	@Override
	public int getChildCount() {
		JDesktopPane desktopPane = (JDesktopPane) getWidget();
		JInternalFrame[] frames = desktopPane.getAllFrames();
		int count = frames == null ? 0 : frames.length;
		return count;
	}

	@Override
	public int getIndexOfChild(Component child) {
		JDesktopPane desktopPane = (JDesktopPane) getWidget();
		JInternalFrame[] frames = desktopPane.getAllFrames();
		for (int i = 0; i < frames.length; i++) {
			if (frames[i] == child)
				return i;
		}
		return -1;
	}

	@Override
	public Component cloneWidget() {
		JDesktopPane copy = (JDesktopPane) super.cloneWidget();
		JDesktopPane pane = (JDesktopPane) getWidget();
		for (JInternalFrame frame : pane.getAllFrames()) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(frame);
			JInternalFrame copy_frame = (JInternalFrame) adapter.cloneWidget();
			copy.add(copy_frame);
			copy_frame.setVisible(true);
		}
		return copy;
	}

	@Override
	public boolean isChildMoveable(Component child) {
		return true;
	}

	@Override
	public boolean isChildVisible(Component child) {
		int i = getIndexOfChild(child);
		return i == 0;
	}

	@Override
	public void showChild(Component widget) {
		JInternalFrame jif = (JInternalFrame) widget;
		jif.toFront();
	}


	@Override
	protected Component newWidget() {
		return new JDesktopPane();
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		JInternalFrame jif = (JInternalFrame)child;
		JDesktopPane jtp = (JDesktopPane) getWidget();
		jif.setBounds((Rectangle)constraints);
		jtp.add(jif);
		jif.setVisible(true);
		clearAllSelected();
		WidgetAdapter.getWidgetAdapter(child).setSelected(true);
		getWidget().validate();
		jif.toFront();
	}

	@Override
	public Object getChildConstraints(Component child) {		
		return child.getBounds();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JDesktopPane.class;
	}
}

