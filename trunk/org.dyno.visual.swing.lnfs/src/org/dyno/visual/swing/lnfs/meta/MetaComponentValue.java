
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

import java.awt.Color;
import java.awt.Font;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class MetaComponentValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public MetaComponentValue() {
		put("foreground", new Color(51, 51, 51));
 		put("background", new Color(238, 238, 238));
		put("font", new Font("Dialog", Font.PLAIN, 11));        
		put("enabled", true);
		put("focusable", true);
		put("visible", true);        		
	}
}

