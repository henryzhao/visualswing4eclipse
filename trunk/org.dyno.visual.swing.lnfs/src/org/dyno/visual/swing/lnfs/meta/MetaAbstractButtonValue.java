
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

import javax.swing.SwingConstants;

public class MetaAbstractButtonValue extends MetaJComponentValue {
	private static final long serialVersionUID = 1L;

	public MetaAbstractButtonValue() {
		put("text", null);
		put("margin", null);
		put("borderPainted", true);
		put("contentAreaFilled", true);
		put("disabledIcon", null);
		put("disabledSelectedIcon", null);
		put("focusPainted", true);
		put("hideActionText", false);
		put("horizontalAlignment", SwingConstants.CENTER);
		put("icon", null);
		put("iconTextGap", 4);
		put("multiClickThreshhold", 0L);
		put("pressedIcon", null);
		put("rolloverEnabled", true);
		put("rolloverIcon", null);
		put("rolloverSelectedIcon", null);
		put("selected", false);
		put("selectedIcon", null);
		put("verticalAlignment", SwingConstants.CENTER);
		put("verticalTextPosition", 0);
		put("mnemonic", 0);
		put("border", SYSTEM_VALUE);
	}
}

