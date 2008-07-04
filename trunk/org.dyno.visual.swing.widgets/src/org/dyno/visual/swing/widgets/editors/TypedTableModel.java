/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.editors;

import javax.swing.table.DefaultTableModel;

public class TypedTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;
	private Class<?>[]columnClasses;
	public TypedTableModel(Object[][] data, Object[] columnNames, Class<?>[]columnClasses) {
		super(data, columnNames);
		this.columnClasses = columnClasses;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}

}
