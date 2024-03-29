
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

package org.dyno.visual.swing.types.renderer;

import javax.swing.ListModel;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ListItemLabelProvider extends LabelProvider {

	public ListItemLabelProvider() {
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element == null)
			return "null";
		if (element instanceof ListModel) {
			StringBuilder builder = new StringBuilder();
			ListModel model = (ListModel) element;
			int size = model.getSize();
			for (int i = 0; i < size; i++) {
				Object object = model.getElementAt(i);
				if (i != 0) {
					builder.append(", ");
				}
				if (object == null) {
					builder.append("null");
				} else {
					builder.append(object.toString());
				}
			}
			return builder.toString();
		} else {
			return element.toString();
		}
	}
}

