/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

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
