
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFormattedTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

import org.dyno.visual.swing.plugin.spi.IEditor;

public class IntegerTextEditor extends JFormattedTextField implements IEditor, ActionListener {
	private static final long serialVersionUID = -4403435758517308113L;
	private ArrayList<ChangeListener> listeners;

	public IntegerTextEditor() {
		super(new NumberFormatter());
		listeners = new ArrayList<ChangeListener>();
		addActionListener(this);
	}

	@Override
	public void addChangeListener(ChangeListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public void removeChangeListener(ChangeListener l) {
		if (listeners.contains(l))
			listeners.remove(l);
	}

	@Override
	public void setFocus() {
		int count = 0;
		while (!isFocusOwner() && count < FOCUSE_REQUEST_LIMIT) {
			requestFocus();
			count++;
		}
		selectAll();
	}
	private Object old_value;
	@Override
	public void setValue(Object value) {
		this.old_value = value;
		super.setValue(value);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ChangeEvent ce = new ChangeEvent(e.getSource());
		fireStateChanged(ce);
	}

	private void fireStateChanged(ChangeEvent e) {
		for (ChangeListener l : listeners) {
			l.stateChanged(e);
		}
	}

	private static final int FOCUSE_REQUEST_LIMIT = 2;

	@Override
	public void validateValue() throws Exception {
	}

	@Override
	public Object getOldValue() {
		return this.old_value;
	}
}

