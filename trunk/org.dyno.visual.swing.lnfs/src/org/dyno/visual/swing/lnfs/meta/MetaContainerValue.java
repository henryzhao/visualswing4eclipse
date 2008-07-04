/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.lnfs.meta;


public class MetaContainerValue extends MetaComponentValue {
	private static final long serialVersionUID = 1L;
	public MetaContainerValue() {
		put("focusCycleRoot", false);
		put("focusTraversalPolicyProvider", null);
	}
}
