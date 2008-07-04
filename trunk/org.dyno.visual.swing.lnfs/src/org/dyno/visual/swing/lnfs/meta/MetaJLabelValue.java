/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.lnfs.meta;

import javax.swing.SwingConstants;

public class MetaJLabelValue extends MetaJComponentValue {
	private static final long serialVersionUID = 1L;
	public MetaJLabelValue() {
		put("alignmentY", 0.5f);
		put("iconTextGap", 4);
		put("inheritsPopupMenu", true);
		put("doubleBuffered", false);
		put("horizontalAlignment", SwingConstants.LEADING);
	}
}
