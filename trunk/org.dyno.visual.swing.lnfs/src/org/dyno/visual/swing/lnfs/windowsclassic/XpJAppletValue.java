
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

package org.dyno.visual.swing.lnfs.windowsclassic;

import java.awt.Color;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class XpJAppletValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public XpJAppletValue() {
		put("focusable", true);
		put("enabled", true);
		put("focusTraversalPolicyProvider", true);
		put("background", Color.white);
		put("foreground", Color.black);
	}
}

