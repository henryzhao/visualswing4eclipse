/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.renderer;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class BorderLabelProvider extends LabelProvider {
	public BorderLabelProvider() {
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}
	@Override
	public String getText(Object element) {
		if (element == null)
			return "null";
		String name = element.getClass().getName();
		int dot = name.lastIndexOf('.');
		if(dot!=-1)
			name = name.substring(dot+1);
		return name;
	}
}
