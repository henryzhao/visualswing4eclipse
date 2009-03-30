
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

package org.dyno.visual.swing.borders;

import java.awt.Insets;

import org.eclipse.jface.viewers.LabelProvider;

public class InsetsLabelProvider extends LabelProvider {
	@Override
	public String getText(Object element) {
		if (element == null)
			return "null";
		if (element instanceof Insets) {
			Insets bounds = (Insets) element;
			return "(" + bounds.top + ", " + bounds.left + ", " + bounds.bottom + ", " + bounds.right + ")";
		}
		return element.toString();
	}
}

