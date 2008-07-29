/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.widgets.editors.TreeModelEditor;

public class JTreeAdapter extends ComplexWidgetAdapter {
	private static int VAR_INDEX = 0;

	public JTreeAdapter() {
		super("jTree" + (VAR_INDEX++));
	}

	protected JComponent createWidget() {
		JTree jtc = new JTree();
		Dimension size = new Dimension(150, 200);
		jtc.setSize(size);
		jtc.doLayout();
		jtc.validate();
		return jtc;
	}

	@Override
	public IEditor getEditorAt(int x, int y) {
		CompositeAdapter parent = getParentAdapter();
		if (parent != null && parent.getWidget() instanceof JScrollPane)
			return new TreeModelEditor((JScrollPane) parent.cloneWidget());
		else
			return null;
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		CompositeAdapter parent = getParentAdapter();
		if (parent != null && parent.getWidget() instanceof JScrollPane) {
			Rectangle bounds = parent.getWidget().getBounds();
			bounds.x = 0;
			bounds.y = 0;
			return bounds;
		}
		Rectangle bounds = getWidget().getBounds();
		bounds.x = 0;
		bounds.y = 0;
		return bounds;
	}

	@Override
	public Object getWidgetValue() {
		JTree tree = (JTree) getWidget();
		return tree.getModel();
	}

	@Override
	public void setWidgetValue(Object value) {
		JTree tree = (JTree) getWidget();
		tree.setModel((TreeModel) value);
	}

	@Override
	protected JComponent newWidget() {
		return new JTree();
	}
}
