
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

import javax.swing.JTree;

public class JTreeAdapter extends ComplexWidgetAdapter {
	public JTreeAdapter() {
		super(null);
	}

	protected Component createWidget() {
		JTree jtc = new JTree();
		jtc.setSize(getInitialSize());
		jtc.doLayout();
		jtc.validate();
		return jtc;
	}

	@Override
	protected Dimension getInitialSize() {
		return new Dimension(150, 200);
	}

	@Override
	protected Component newWidget() {
		return new JTree();
	}
	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JTree.class;
	}
}

