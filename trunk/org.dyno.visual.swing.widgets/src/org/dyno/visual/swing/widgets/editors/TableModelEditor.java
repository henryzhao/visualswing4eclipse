/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.editors;

import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;

import org.dyno.visual.swing.plugin.spi.Editor;

public class TableModelEditor implements Editor {
	private List<ChangeListener> listeners;
	private TableModelPanel tmPanel;
	public TableModelEditor(JScrollPane jsp){
		listeners = new ArrayList<ChangeListener>();
		tmPanel = new TableModelPanel(jsp);
	}
	@Override
	public void addChangeListener(ChangeListener l) {
		if(!listeners.contains(l)){
			listeners.add(l);
		}
	}
	@Override
	public Component getComponent() {
		return tmPanel;
	}

	@Override
	public Object getValue() {
		return tmPanel.getTableModel();
	}
	@Override
	public void removeChangeListener(ChangeListener l) {
		if(listeners.contains(l)){
			listeners.remove(l);
		}
	}
	@Override
	public void setFocus() {
		tmPanel.setFocus();
	}
	@Override
	public void setFont(Font f) {
		tmPanel.setFont(f);
	}
	@Override
	public void setValue(Object v) {
		tmPanel.setTableModel((TableModel)v);
	}
	@Override
	public void validateValue() throws Exception {
	}
}