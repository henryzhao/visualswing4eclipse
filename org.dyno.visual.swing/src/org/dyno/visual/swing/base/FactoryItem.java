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

package org.dyno.visual.swing.base;
/**
 * 
 * FactoryItem
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class FactoryItem {
	private String objectName;
	@SuppressWarnings("unchecked")
	private Class objectClazz;
	private IFactory factory;

	@SuppressWarnings("unchecked")
	public FactoryItem(String name, Class clazz, IFactory factory) {
		this.objectName = name;
		this.factory = factory;
		this.objectClazz = clazz;
	}

	public String getObjectName() {
		return objectName;
	}

	public IFactory getFactory() {
		return factory;
	}

	public boolean isSelected(Object value) {
		if (value == null && objectClazz == null)
			return true;
		else
			return value.getClass() == objectClazz;
	}
}

