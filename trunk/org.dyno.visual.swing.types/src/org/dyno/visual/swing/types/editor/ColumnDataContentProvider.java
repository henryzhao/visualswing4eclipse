/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ColumnDataContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof ColumnDataInput) {
			List<ColumnDataRow> data = new ArrayList<ColumnDataRow>();
			for (int i = 0; i < 4; i++)
				data.add(new ColumnDataRow());
			ColumnDataRow[] array = new ColumnDataRow[data.size()];
			return data.toArray(array);
		} else if(inputElement instanceof ColumnDataRow){
			List<String> data = new ArrayList<String>();
			for (int i = 0; i < 4; i++)
				data.add("string");
			String[] array = new String[data.size()];
			return data.toArray(array);
		}
		return null;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
