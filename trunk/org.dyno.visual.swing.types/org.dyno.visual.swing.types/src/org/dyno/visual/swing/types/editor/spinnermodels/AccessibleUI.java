/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor.spinnermodels;

import org.eclipse.swt.widgets.Control;

public interface AccessibleUI {
	Control getAccessibleUI();
	void setValue(Object value);
	Object getValue();
	String isInputValid();
}
