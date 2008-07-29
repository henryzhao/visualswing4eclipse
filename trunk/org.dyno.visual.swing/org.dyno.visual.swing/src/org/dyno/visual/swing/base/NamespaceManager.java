/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.base;

import org.dyno.visual.swing.editors.DefaultNamespaceManager;
/**
 * 
 * NamespaceManager
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public abstract class NamespaceManager {
	private static NamespaceManager instance;
	public static NamespaceManager getInstance(){
		if(instance==null){
			initialize();
		}
		return instance;
	}
	private static void initialize() {
		instance = new DefaultNamespaceManager();
	}
	public abstract String getNameFromFieldName(String fieldName);
	public abstract String getFieldName(String name);
	public abstract String getGetMethodName(String name);
	public abstract boolean isGetMethodName(String name);
	public abstract String getFieldNameFromGetMethodName(String getMethodName);
	public abstract String getCapitalName(String name);
	public abstract void removeName(String name);
	public abstract void addName(String name);
	public abstract boolean hasDeclaredName(String newName);
}
