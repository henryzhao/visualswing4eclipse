/*
 * Item.java
 *
 * Created on 2007-8-19, 16:38:20
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dyno.visual.swing.base;

import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

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