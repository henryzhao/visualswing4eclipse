
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

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.widgets.editors.TableModelEditor;

public class JTableAdapter extends ComplexWidgetAdapter {
	private static int VAR_INDEX = 0;

	public JTableAdapter() {
		super("jTable" + (VAR_INDEX++));
	}

	protected Component createWidget() {
		JTable jtc = new JTable();
		jtc.setModel(new DefaultTableModel(new Object[][] { { "0x0", "0x1" }, { "1x0", "1x1" } }, new Object[] { "Title 0", "Title 1" }));
		Dimension size = new Dimension(200, 150);
		jtc.setSize(size);
		jtc.doLayout();
		jtc.validate();
		return jtc;
	}

	@Override
	public IEditor getEditorAt(int x, int y) {
		CompositeAdapter parent = getParentAdapter();
		if (parent != null && parent.getWidget() instanceof JScrollPane)
			return new TableModelEditor((JScrollPane) parent.cloneWidget());
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
		JTable table = (JTable) getWidget();
		return table.getModel();
	}

	@Override
	public void setWidgetValue(Object value) {
		JTable table = (JTable) getWidget();
		table.setModel((TableModel) value);
	}

	@Override
	protected Component newWidget() {
		return new JTable();
	}
	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JTable.class;
	}
}

