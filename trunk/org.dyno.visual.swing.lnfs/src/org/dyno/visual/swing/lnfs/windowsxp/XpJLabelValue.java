
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

public class XpJLabelValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public XpJLabelValue() {
		put("horizontalAlignment", 10);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("enabled", true);
		put("focusable", true);
		put("iconTextGap", 4);
		put("alignmentY", 0.5f);
		put("inheritsPopupMenu", true);
		put("requestFocusEnabled", true);
	}
}

