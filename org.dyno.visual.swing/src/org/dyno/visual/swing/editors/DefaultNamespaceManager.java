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

package org.dyno.visual.swing.editors;

import java.util.HashMap;

import org.dyno.visual.swing.base.NamespaceManager;

/**
 * 
 * DefaultNamespaceManager
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class DefaultNamespaceManager extends NamespaceManager {
	private static HashMap<String, String> used_names = new HashMap<String, String>();
	private static String GET = "get";

	public String getGetMethodName(String name) {
		return GET + Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

	public String getNameFromFieldName(String name) {
		return name;
	}

	public String getFieldName(String name) {
		return name;
	}

	@Override
	public String getFieldNameFromGetMethodName(String getMethodName) {
		String name = getMethodName.substring(GET.length());
		name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
		return name;
	}

	@Override
	public boolean isGetMethodName(String name) {
		return name.startsWith(GET);
	}

	@Override
	public String getCapitalName(String name) {
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

	@Override
	public void addName(String name) {
		used_names.put(name, name);
	}

	@Override
	public void removeName(String name) {
		used_names.remove(name);
	}

	@Override
	public boolean hasDeclaredName(String newName) {
		return used_names.get(newName) != null;
	}
}

