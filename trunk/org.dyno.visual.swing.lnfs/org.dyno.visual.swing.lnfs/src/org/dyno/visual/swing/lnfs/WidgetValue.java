/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.lnfs;

import java.util.HashMap;

public abstract class WidgetValue extends HashMap<String, Object>{
	private static final long serialVersionUID = 1L;

	public Object getDefaultValue(String propertyName){
		return get(propertyName);
	}
}
