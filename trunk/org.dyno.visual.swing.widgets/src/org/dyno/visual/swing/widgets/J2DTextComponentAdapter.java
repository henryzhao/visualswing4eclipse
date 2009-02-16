
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

import javax.swing.text.JTextComponent;

public abstract class J2DTextComponentAdapter extends ComplexWidgetAdapter {

	public J2DTextComponentAdapter(String varName) {
		super(varName);
	}

	protected Component createWidget() {
		JTextComponent jtc = createTextComponent();
		requestGlobalNewName();
		jtc.setText(getName());
		Dimension size = getInitialSize();
		jtc.setSize(size);
		jtc.doLayout();
		jtc.validate();
		return jtc;
	}
	@Override
	public String getBasename() {
		String className = getWidgetClass().getName();
		int dot = className.lastIndexOf('.');
		if (dot != -1)
			className = className.substring(dot + 1);
		return Character.toLowerCase(className.charAt(0)) + className.substring(1);
	}	
	@Override
	protected Dimension getInitialSize() {
		return new Dimension(100, 80);
	}

	protected abstract JTextComponent createTextComponent();

	@Override
	public void requestNewName() {
		super.requestNewName();
		JTextComponent jtc = (JTextComponent) getWidget();
		jtc.setText(getName());
	}
}

