
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

import java.awt.Font;
import java.util.Comparator;

public class FontComparator implements Comparator<Font> {
	@Override
	public int compare(Font o1, Font o2) {
		String f1 = o1.getFamily();
		String f2 = o2.getFamily();
		int style1 = o1.getStyle();
		int style2 = o2.getStyle();
		int size1 = o1.getSize();
		int size2 = o2.getSize();
		return f1.equals(f2)&&style1==style2&&size1==size2?0:1;
	}
}

