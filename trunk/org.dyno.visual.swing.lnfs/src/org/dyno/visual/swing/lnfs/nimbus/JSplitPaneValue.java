
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

package org.dyno.visual.swing.lnfs.nimbus;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JSplitPaneValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JSplitPaneValue() {
		put("dividerLocation", 53);
		put("dividerSize", 10);
		put("orientation", 1);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("lastDividerLocation", -1);
		put("opaque", false);
		put("enabled", true);
		put("focusable", true);
		put("requestFocusEnabled", true);
		put("continuousLayout", true);
	}
}

