
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
import java.awt.TextArea;

import javax.swing.event.ChangeListener;

import org.dyno.visual.swing.plugin.spi.IEditor;

public class TextAreaEditor extends TextArea implements IEditor {
	private static final long serialVersionUID = -4403435758517308113L;

	public TextAreaEditor() {
	}

	
	public void addChangeListener(ChangeListener l) {
	}

	
	public Component getComponent() {
		return this;
	}

	
	public Object getValue() {
		return getText();
	}

	
	public void removeChangeListener(ChangeListener l) {
	}
	private Object old_value;
	
	public void setValue(Object v) {
		this.old_value = v;
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

	private static final int FOCUSE_REQUEST_LIMIT = 2;

	
	public void validateValue() throws Exception {
	}

	
	public Object getOldValue() {
		return old_value;
	}
}

