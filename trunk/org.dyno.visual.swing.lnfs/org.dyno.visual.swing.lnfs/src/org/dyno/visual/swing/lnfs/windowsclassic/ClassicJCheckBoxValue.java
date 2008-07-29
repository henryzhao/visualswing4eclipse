/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.lnfs.windowsclassic;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class ClassicJCheckBoxValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public ClassicJCheckBoxValue() {
		put("borderPainted", false);
		put("horizontalAlignment", 10);
		put("opaque", true);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("focusPainted", true);
		put("contentAreaFilled", true);
		put("focusable", true);
		put("enabled", true);
		put("iconTextGap", 4);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("rolloverEnabled", false);		
	}
}
