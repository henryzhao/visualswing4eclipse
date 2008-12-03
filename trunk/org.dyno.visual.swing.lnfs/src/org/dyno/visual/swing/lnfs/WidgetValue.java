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

import org.dyno.visual.swing.plugin.spi.ISystemValue;

public abstract class WidgetValue extends HashMap<String, Object>{
	protected static class SystemValue implements ISystemValue{
		private static final long serialVersionUID = 1L;
	}
	protected static SystemValue SYSTEM_VALUE=new SystemValue();
	
	private static final long serialVersionUID = 1L;

	public Object getDefaultValue(String propertyName){
		return get(propertyName);
	}
}
