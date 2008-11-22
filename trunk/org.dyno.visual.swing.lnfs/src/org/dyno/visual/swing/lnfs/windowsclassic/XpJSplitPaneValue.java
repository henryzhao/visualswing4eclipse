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

public class XpJSplitPaneValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public XpJSplitPaneValue() {
		put("dividerLocation", 84);
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
