/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.lnfs.meta;


public class MetaJScrollPaneValue extends MetaJComponentValue {
	private static final long serialVersionUID = 1L;
	public MetaJScrollPaneValue() {
		put("horizontalScrollBarPolicy", 30);
		put("verticalScrollBarPolicy", 20);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("wheelScrollingEnabled", true);
		put("doubleBuffered", false);
	}
}
