
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

package org.dyno.visual.swing.lnfs.meta;

import javax.swing.JSplitPane;

public class MetaJSplitPaneValue extends MetaJComponentValue {
	private static final long serialVersionUID = 1L;

	public MetaJSplitPaneValue() {
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("doubleBuffered", false);
		put("dividerLocation", 59);
		put("dividerSize", 10);
		put("orientation", JSplitPane.HORIZONTAL_SPLIT);
		put("verifyInputWhenFocusTarget", true);
		put("lastDividerLocation", -1);
		put("alignmentX", 0.0f);
		put("alignmentY", 0.0f);
		put("border", SYSTEM_VALUE);
	}
}

