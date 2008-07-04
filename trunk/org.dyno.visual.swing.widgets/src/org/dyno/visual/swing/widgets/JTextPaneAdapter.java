/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

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

}
