/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.lnfs.meta;

import java.awt.Insets;

public class MetaJToolBarValue extends MetaContainerValue {
	private static final long serialVersionUID = 1L;
	public MetaJToolBarValue() {
		put("floatable", true);
		put("visible", true);
		put("borderPainted", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("margin", new Insets(0, 0, 0, 0));
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("requestFocusEnabled", true); 		
	}
}
