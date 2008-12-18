
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

