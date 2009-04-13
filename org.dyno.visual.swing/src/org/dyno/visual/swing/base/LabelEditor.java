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

package org.dyno.visual.swing.base;

import java.awt.Component;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.dyno.visual.swing.plugin.spi.IEditor;
/**
 * 
 * LabelEditor
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class LabelEditor extends TextField implements IEditor, ActionListener {
	private static final long serialVersionUID = -4403435758517308113L;
	private ArrayList<ChangeListener> listeners;

	public LabelEditor() {
		listeners = new ArrayList<ChangeListener>();
		addActionListener(this);
	}
	public void addChangeListener(ChangeListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}
	public Component getComponent() {
		return this;
	}
	public Object getValue() {
		return getText();
	}
	public void removeChangeListener(ChangeListener l) {
		if (listeners.contains(l))
			listeners.remove(l);
	}
	private Object old;
	public void setValue(Object v) {
		this.old = v;
		setText(v == null ? "" : v.toString());
	}
	public void setFocus() {
		int count = 0;
		while (!isFocusOwner() && count < FOCUSE_REQUEST_LIMIT) {
			requestFocus();
			count++;
		}
		selectAll();
	}
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

	public void validateValue() throws Exception {
	}

	public Object getOldValue() {
		return this.old;
	}
}

