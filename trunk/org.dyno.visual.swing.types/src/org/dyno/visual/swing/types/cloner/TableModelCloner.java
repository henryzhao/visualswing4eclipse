package org.dyno.visual.swing.types.cloner;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.dyno.visual.swing.plugin.spi.ICloner;

public class TableModelCloner implements ICloner {

	@SuppressWarnings("unchecked")
	@Override
	public Object clone(Object object) {
		TableModel tableModel = (TableModel) object;
		int column = tableModel.getColumnCount();
		int row = tableModel.getRowCount();
		String[] names = new String[column];
		for (int i = 0; i < column; i++)
			names[i] = tableModel.getColumnName(i);
		Object[][] data = new Object[row][];
		for (int i = 0; i < row; i++) {
			data[i] = new Object[column];
			for (int j = 0; j < column; j++) {
				data[i][j] = tableModel.getValueAt(i, j);
			}
		}
		final Class[]columnClasses = new Class[column];
		for(int i=0;i<column;i++){
			columnClasses[i]=tableModel.getColumnClass(i);
		}		
		return new DefaultTableModel(data, names){
			private static final long serialVersionUID = 1L;
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnClasses[columnIndex];
			}
		};
	}

}
