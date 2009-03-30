
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

import javax.swing.WindowConstants;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class MetaJFrameValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public MetaJFrameValue() {
		put("focusable", true);
		put("enabled", true);
		put("focusCycleRoot",true);    
		put("defaultCloseOperation", WindowConstants.HIDE_ON_CLOSE);
		put("focusableWindowState", true);
		put("resizable", true);
	}
}

