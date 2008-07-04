/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.lnfs.meta;


public class MetaJInternalFrameValue extends MetaContainerValue {
	private static final long serialVersionUID = 1L;
	public MetaJInternalFrameValue() {
		put("defaultCloseOperation", 2);
		put("title", "");
		put("visible", false);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("focusable", true);
		put("enabled", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("focusCycleRoot", true);		
	}
}
