/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.comparator;

import java.awt.Insets;
import java.util.Comparator;

public class InsetsComparator implements Comparator<Insets> {
	@Override
	public int compare(Insets o1, Insets o2) {
		return o1.top == o2.top && o1.left == o2.left && o1.bottom == o2.bottom && o1.right == o2.right ? 0 : 1;
	}

}
