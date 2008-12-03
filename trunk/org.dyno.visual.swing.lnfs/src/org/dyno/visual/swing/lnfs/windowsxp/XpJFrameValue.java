
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

import javax.swing.WindowConstants;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class XpJFrameValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public XpJFrameValue() {
		put("defaultCloseOperation", WindowConstants.HIDE_ON_CLOSE);
		put("focusCycleRoot", true);
		put("enabled", true);
		put("focusable", true);
	}
}

