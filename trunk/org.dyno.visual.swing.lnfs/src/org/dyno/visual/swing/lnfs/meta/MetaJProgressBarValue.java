/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.lnfs.meta;

public class MetaJProgressBarValue extends MetaJComponentValue {
	private static final long serialVersionUID = 1L;

	public MetaJProgressBarValue() {
		put("value", 0);
		put("maximum", 100);
		put("visible", true);
		put("borderPainted", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("string", "0%");
		put("doubleBuffered", false);
	}
}
