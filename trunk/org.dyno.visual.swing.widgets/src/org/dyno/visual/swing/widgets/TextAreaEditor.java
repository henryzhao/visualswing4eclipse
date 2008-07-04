/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.TextArea;

import javax.swing.event.ChangeListener;

import org.dyno.visual.swing.plugin.spi.Editor;

public class TextAreaEditor extends TextArea implements Editor {
	private static final long serialVersionUID = -4403435758517308113L;

	public TextAreaEditor() {
	}

	@Override
	public void addChangeListener(ChangeListener l) {
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public Object getValue() {
		return getText();
	}

	@Override
	public void removeChangeListener(ChangeListener l) {
	}

	@Override
	public void setValue(Object v) {
		setText(v == null ? "" : v.toString());
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

	private static final int FOCUSE_REQUEST_LIMIT = 2;

	@Override
	public void validateValue() throws Exception {
	}
}
