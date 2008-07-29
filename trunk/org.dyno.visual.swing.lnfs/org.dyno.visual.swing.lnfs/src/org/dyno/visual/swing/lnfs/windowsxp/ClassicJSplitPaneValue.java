/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.lnfs.windowsxp;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class ClassicJSplitPaneValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public ClassicJSplitPaneValue() {
		put("dividerLocation", 86);
		put("dividerSize", 5);
		put("orientation", 1);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("lastDividerLocation", -1);
		put("opaque", true);
		put("enabled", true);
		put("focusable", true);
		put("requestFocusEnabled", true);
	}
}
