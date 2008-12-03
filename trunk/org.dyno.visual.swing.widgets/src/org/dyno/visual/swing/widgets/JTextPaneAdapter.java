
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

import javax.swing.JTextPane;
import javax.swing.text.JTextComponent;

public class JTextPaneAdapter extends J2DTextComponentAdapter {
	public JTextPaneAdapter() {
		super("jTextPane");
	}

	@Override
	protected JTextComponent createTextComponent() {
		return new JTextPane();
	}

	@Override
	protected Component newWidget() {
		return new JTextPane();
	}
	@Override
	public Class getWidgetClass() {
		return JTextPane.class;
	}

}

