
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

package org.dyno.visual.swing.types.editor.spinnermodels;

import org.dyno.visual.swing.types.editor.spinnermodels.types.NumberType;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class NumberTypeContentProvider implements IStructuredContentProvider {
	private NumberType[]types;
	
	public Object[] getElements(Object inputElement) {
		return types;
	}

	
	public void dispose() {
		this.types = null;
	}

	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.types = (NumberType[]) newInput;
	}

}

