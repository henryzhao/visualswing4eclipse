
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

