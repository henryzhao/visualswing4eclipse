/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.lnfs.meta;

public class MetaJComponentValue extends MetaContainerValue {
	private static final long serialVersionUID = 1L;
	public MetaJComponentValue() {
		put("border", null);
		put("opaque", false);
		put("toolTipText", null);
		put("doubleBuffered", true);
		put("alignmentX", 0.0f);
		put("alignmentY", 0.0f);
		put("autoscrolls", false);
		put("debugGraphicsOptions", 0); 
		put("inheritsPopupMenu", false);
		put("requestFocusEnabled", true);
		put("verifyInputWhenFocusTarget", true);        		
	}
}
