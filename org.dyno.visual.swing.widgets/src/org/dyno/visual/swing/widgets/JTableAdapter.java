
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

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("unchecked")
public class JTableAdapter extends ComplexWidgetAdapter {
	public JTableAdapter() {
		super(null);
	}

	protected Component createWidget() {
		JTable jtc = new JTable();
		jtc.setModel(new DefaultTableModel(new Object[][] { { Messages.JTableAdapter_0, Messages.JTableAdapter_1 }, { Messages.JTableAdapter_2, Messages.JTableAdapter_3 } }, new Object[] { Messages.JTableAdapter_Title_0, Messages.JTableAdapter_Title_1 }));
		jtc.setSize(getInitialSize());
		jtc.doLayout();
		jtc.validate();
		return jtc;
	}
	protected Dimension getInitialSize(){
		return new Dimension(200, 150);
	}

	@Override
	protected Component newWidget() {
		return new JTable();
	}
	@Override
	public Class getWidgetClass() {
		return JTable.class;
	}
}

