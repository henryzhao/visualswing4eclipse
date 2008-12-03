
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

package org.dyno.visual.swing.lnfs.motif;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JListValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JListValue() {
		put("selectionMode", 2);
		put("model", null);
		put("visible", true);
		put("fixedCellHeight", -1);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("autoscrolls", true);
		put("enabled", true);
		put("focusable", true);
		put("selectedIndex", -1);
		put("visibleRowCount", 8);
		put("alignmentX", 0.5f);
		put("fixedCellWidth", -1);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
	}
}

