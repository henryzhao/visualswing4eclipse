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

import javax.swing.JEditorPane;
import javax.swing.text.JTextComponent;

public class JEditorPaneAdapter extends J2DTextComponentAdapter {
	public JEditorPaneAdapter() {
		super("jEditorPane");
	}

	@Override
	protected JTextComponent createTextComponent() {
		return new JEditorPane();
	}

	@Override
	protected Component newWidget() {
		return new JEditorPane();
	}

}
