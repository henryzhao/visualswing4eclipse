/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor.spinnermodels;

import org.dyno.visual.swing.types.editor.spinnermodels.types.NumberType;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class NumberTypeContentProvider implements IStructuredContentProvider {
	private NumberType[]types;
	@Override
	public Object[] getElements(Object inputElement) {
		return types;
	}

	@Override
	public void dispose() {
		this.types = null;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.types = (NumberType[]) newInput;
	}

}
