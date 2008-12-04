
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

public class MetaJTextFieldValue extends MetaJComponentValue {
	private static final long serialVersionUID = 1L;

	public MetaJTextFieldValue() {
		put("opaque", true);
		put("alignmentY", 0.5f);	
		put("alignmentX", 0.5f);	
		put("editable", true);
		put("horizontalAlignment", 10);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("autoscrolls", true);
		put("enabled", true);
		put("focusable", true);
		put("requestFocusEnabled", true);
		put("doubleBuffered", false);
		put("border", SYSTEM_VALUE);
	}
}

