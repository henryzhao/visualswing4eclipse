/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types;

import org.eclipse.jface.viewers.LabelProvider;

public class IconLabelProvider extends LabelProvider{
	@Override
	public String getText(Object element) {
		if (element == null)
			return "null";
		return element.toString();
	}
}
