
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

package org.dyno.visual.swing.widgets.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.Point;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.ILayoutBean;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class GridBagLayoutAdapter extends LayoutAdapter implements ILayoutBean {

	@Override
	public void initConainerLayout(Container container, IProgressMonitor monitor) {
		container.setLayout(new GridBagLayout());
	}

	@Override
	public boolean dragEnter(Point p) {
		return super.dragEnter(p);
	}

	@Override
	public boolean dragExit(Point p) {
		return super.dragExit(p);
	}

	@Override
	public boolean dragOver(Point p) {
		return super.dragOver(p);
	}

	@Override
	public boolean drop(Point p) {
		WidgetAdapter parent = WidgetAdapter.getWidgetAdapter(container);
		parent.clearAllSelected();
		for (WidgetAdapter todrop : parent.getDropWidget()) {
			container.add(todrop.getComponent(), new GridBagConstraints());
			todrop.setSelected(true);
		}
		parent.getRootAdapter().getWidget().validate();
		return super.drop(p);
	}

	@Override
	public boolean cloneLayout(JComponent panel) {
		GridBagLayout layout = (GridBagLayout) container.getLayout();
		panel.setLayout(copyLayout(panel));
		int count = container.getComponentCount();
		for (int i = 0; i < count; i++) {
			JComponent child = (JComponent) container.getComponent(i);
			WidgetAdapter cAdapter = WidgetAdapter.getWidgetAdapter(child);
			panel.add(cAdapter.cloneWidget(), layout.getConstraints(child));
		}
		return true;
	}

	@Override
	protected LayoutManager copyLayout(Container con) {
		GridBagLayout copy = new GridBagLayout();
		return copy;
	}

	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		String layoutName = imports.addImport("java.awt.GridBagLayout");
		return "new " + layoutName + "()";
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		container.add(child, constraints);
	}

	@Override
	public Object getChildConstraints(Component child) {
		GridBagLayout layout = (GridBagLayout) container.getLayout();
		return layout.getConstraints(child);
	}
}

