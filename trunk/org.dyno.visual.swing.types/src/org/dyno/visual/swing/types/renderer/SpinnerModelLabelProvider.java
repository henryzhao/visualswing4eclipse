
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

import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class SpinnerModelLabelProvider extends LabelProvider {

	public SpinnerModelLabelProvider() {
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element == null)
			return "null";
		if (element instanceof SpinnerModel) {
			if (element instanceof SpinnerDateModel) {
				return "SpinnerDateModel";
			} else if (element instanceof SpinnerNumberModel) {
				return "SpinnerNumberModel";
			} else if (element instanceof SpinnerListModel) {
				return "SpinnerListModel";
			}
		}
		return element.toString();
	}
}

