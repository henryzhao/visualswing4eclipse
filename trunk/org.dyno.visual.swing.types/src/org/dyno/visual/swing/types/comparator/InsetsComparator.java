
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

import java.awt.Insets;
import java.util.Comparator;

public class InsetsComparator implements Comparator<Insets> {
	
	public int compare(Insets o1, Insets o2) {
		return o1.top == o2.top && o1.left == o2.left && o1.bottom == o2.bottom && o1.right == o2.right ? 0 : 1;
	}

}

