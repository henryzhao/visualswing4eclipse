
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

import javax.swing.DefaultListModel;
import javax.swing.JList;

@SuppressWarnings("unchecked")
public class JListAdapter extends ComplexWidgetAdapter {
	public JListAdapter() {
		super(null);
	}

	protected Component createWidget() {
		JList jc = new JList();
		DefaultListModel dlm = new DefaultListModel();
		dlm.addElement(Messages.JListAdapter_Item_0);
		dlm.addElement(Messages.JListAdapter_Item_1);
		dlm.addElement(Messages.JListAdapter_Item_2);
		dlm.addElement(Messages.JListAdapter_Item_3);
		jc.setModel(dlm);
		jc.setSize(getInitialSize());
		jc.doLayout();
		jc.validate();
		return jc;
	}

	@Override
	protected Dimension getInitialSize() {
		return new Dimension(100, 135);
	}

	@Override
	protected Component newWidget() {
		return new JList();
	}
	@Override
	public Class getWidgetClass() {
		return JList.class;
	}

}

