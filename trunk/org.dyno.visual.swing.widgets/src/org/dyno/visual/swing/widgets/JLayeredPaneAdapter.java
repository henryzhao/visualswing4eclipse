/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JLayeredPaneAdapter extends CompositeAdapter {
	private static int VAR_INDEX = 0;

	public JLayeredPaneAdapter() {
		super("jLayeredPane" + (VAR_INDEX++));
	}

	@Override
	protected JComponent createWidget() {
		JLayeredPane jlp = new JLayeredPane();
		Dimension size = new Dimension(100, 100);
		jlp.setSize(size);
		return jlp;
	}

	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		return null;
	}

}
