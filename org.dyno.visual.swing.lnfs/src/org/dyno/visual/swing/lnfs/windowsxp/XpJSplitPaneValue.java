
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

package org.dyno.visual.swing.lnfs.windowsxp;

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
		put("border", SYSTEM_VALUE);
	}
}

