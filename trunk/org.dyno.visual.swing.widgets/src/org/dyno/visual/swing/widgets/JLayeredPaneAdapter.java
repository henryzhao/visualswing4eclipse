
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

import javax.swing.JLayeredPane;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JLayeredPaneAdapter extends CompositeAdapter {
	private static int VAR_INDEX = 0;

	public JLayeredPaneAdapter() {
		super("jLayeredPane" + (VAR_INDEX++));
	}

	@Override
	protected Component createWidget() {
		JLayeredPane jlp = new JLayeredPane();
		Dimension size = new Dimension(100, 100);
		jlp.setSize(size);
		return jlp;
	}

	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		return null;
	}

	@Override
	protected Component newWidget() {
		return new JLayeredPane();
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		JLayeredPane layeredPane = (JLayeredPane)getWidget();
		layeredPane.setLayer(child, constraints==null?0:((Integer)constraints).intValue());
		layeredPane.add(child);
	}

	@Override
	public Object getChildConstraints(Component child) {
		JLayeredPane layeredPane = (JLayeredPane)getWidget();
		return layeredPane.getLayer(child);
	}

	@Override
	public Class getWidgetClass() {
		return JLayeredPane.class;
	}

}

