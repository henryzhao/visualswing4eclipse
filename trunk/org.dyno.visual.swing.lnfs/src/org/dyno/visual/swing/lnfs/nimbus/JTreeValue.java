
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

import java.awt.Color;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JTreeValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JTreeValue() {
		put("visible", true);
		put("expandsSelectedPaths", true);
		put("toggleClickCount", 2);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("focusable", true);
		put("enabled", true);
		put("visibleRowCount", 20);
		put("scrollsOnExpand", true);
		put("rowHeight", 0);
		put("rootVisible", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("showsRootHandles", true);
		put("foreground", new Color(0, 0, 0));
		put("border", SYSTEM_VALUE);
	}
}

