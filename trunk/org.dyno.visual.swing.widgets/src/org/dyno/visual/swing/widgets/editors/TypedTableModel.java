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
