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

import org.dyno.visual.swing.lnfs.WidgetValue;

public class MetaJMenuValue extends WidgetValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetaJMenuValue() {
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("borderPainted", true);
		put("horizontalAlignment", 10);
		put("contentAreaFilled", true);
		put("focusable", true);
		put("enabled", true);
		put("iconTextGap", 4);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
	}
}

