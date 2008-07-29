/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.renderer;

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
