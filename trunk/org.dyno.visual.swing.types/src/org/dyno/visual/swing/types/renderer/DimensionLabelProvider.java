/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.renderer;

import java.awt.Dimension;

import org.eclipse.jface.viewers.LabelProvider;

/**
 * 
 * @author William Chen
 */
public class DimensionLabelProvider extends LabelProvider {
	private static final long serialVersionUID = -4403435758517308113L;

	@Override
	public String getText(Object element) {
		if (element == null)
			return "null";
		if (element instanceof Dimension) {
			Dimension dim = (Dimension) element;
			return "("+dim.width+", "+dim.height+")";
		}
		return element.toString();
	}
}
