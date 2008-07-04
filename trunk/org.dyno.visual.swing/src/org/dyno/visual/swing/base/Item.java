/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.base;

import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
/**
 * 
 * Item
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class Item {

	private Object value;
	private String name;
	private String code;

	public Item(String name, Object value, String code) {
		this.name = name;
		this.value = value;
		this.code = code;
	}

	public Object getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Item) {
			Item a = (Item) o;
			Object av = a.getValue();
			if (value == null) {
				if (av == null)
					return true;
				else
					return false;
			} else {
				if (av == null)
					return false;
				else
					return value.equals(av);
			}
		} else {
			return false;
		}
	}

	public String getCode(ImportRewrite imports) {
		int dot = code.lastIndexOf('.');
		String className = code;
		String ext = code;
		if(dot!=-1){
			className = code.substring(0, dot);
			ext = code.substring(dot+1);
			String strImport = imports.addImport(className);		
			return strImport+"."+ext;
		}else{
			return ext;
		}
	}

	public void setCode(String code) {
		this.code = code;
	}
}