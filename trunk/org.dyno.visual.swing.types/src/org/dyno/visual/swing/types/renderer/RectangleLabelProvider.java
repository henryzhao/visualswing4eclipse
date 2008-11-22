/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.renderer;

import java.awt.Rectangle;

import org.eclipse.jface.viewers.LabelProvider;

public class RectangleLabelProvider extends LabelProvider {
	@Override
	public String getText(Object element) {
		if (element == null)
			return "null";
		if (element instanceof Rectangle) {
			Rectangle bounds = (Rectangle) element;
			return "(" + bounds.x + ", " + bounds.y + ", " + bounds.width + ", " + bounds.height + ")";
		}
		return element.toString();
	}
}
