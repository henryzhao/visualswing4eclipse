
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

package org.dyno.visual.swing.types.comparator;

import java.util.Comparator;

import javax.swing.SpinnerModel;

import org.dyno.visual.swing.types.editor.spinnermodels.SpinnerModelType;
import org.dyno.visual.swing.types.editor.spinnermodels.SpinnerNumberModelType;

public class SpinnerModelComparator implements Comparator<SpinnerModel> {
	@SuppressWarnings("unchecked")
	@Override
	public int compare(SpinnerModel o1, SpinnerModel o2) {
		if (o1 == null) {
			if (o2 == null) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if (o2 == null) {
				return 1;
			} else {
				Class clazz1 = o1.getClass();
				Class clazz2 = o2.getClass();
				if (clazz1 == clazz2) {
					SpinnerModelType type = SpinnerNumberModelType.getSpinnerModelType(o1);
					return type.compare(o1, o2);
				} else {
					return 1;
				}
			}
		}
	}
}

