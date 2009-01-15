
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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JComboBoxAdapter extends WidgetAdapter {
	public JComboBoxAdapter() {
		super(null);
	}

	protected Component createWidget() {
		JComboBox jc = new JComboBox();
		jc.setModel(new DefaultComboBoxModel(new Object[] { Messages.JComboBoxAdapter_Item_0, Messages.JComboBoxAdapter_Item_1, Messages.JComboBoxAdapter_Item_2, Messages.JComboBoxAdapter_Item_3 }));
		Dimension size = jc.getPreferredSize();
		jc.setSize(size);
		jc.doLayout();
		jc.validate();
		return jc;
	}

	@Override
	protected Component newWidget() {
		return new JComboBox();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JComboBox.class;
	}

}

