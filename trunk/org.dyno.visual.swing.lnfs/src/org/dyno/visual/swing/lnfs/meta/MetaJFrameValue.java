/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

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
	}
}
