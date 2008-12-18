
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

public class ClassicJEditorPaneValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public ClassicJEditorPaneValue() {
		put("contentType", "text/plain");
		put("editable", true);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("autoscrolls", true);
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("focusCycleRoot", true);
		put("requestFocusEnabled", true);
		put("border", SYSTEM_VALUE);
	}
}

