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

package org.dyno.visual.swing.widgets.editors;

import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;

import org.dyno.visual.swing.plugin.spi.IEditor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class TableModelEditor implements IEditor {
	private List<ChangeListener> listeners;
	private TableModelPanel tmPanel;

	public TableModelEditor(WidgetAdapter adapter, JScrollPane jsp) {
		listeners = new ArrayList<ChangeListener>();
		tmPanel = new TableModelPanel(adapter, jsp);
	}

	
	public void addChangeListener(ChangeListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	
	public Component getComponent() {
		return tmPanel;
	}

	
	public Object getValue() {
		return tmPanel.getTableModel();
	}

	
	public void removeChangeListener(ChangeListener l) {
		if (listeners.contains(l)) {
			listeners.remove(l);
		}
	}

	
	public void setFocus() {
		tmPanel.setFocus();
	}

	
	public void setFont(Font f) {
		tmPanel.setFont(f);
	}

	private Object old;

	
	public void setValue(Object v) {
		old = v;
		tmPanel.setTableModel((TableModel) v);
	}

	
	public void validateValue() throws Exception {
	}

	
	public Object getOldValue() {
		return old;
	}
}
