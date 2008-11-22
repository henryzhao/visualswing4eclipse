/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.base;

import java.util.Stack;

import javax.swing.MenuElement;

public class MenuSelectionManager {
	private static MenuSelectionManager defaultManager;
	private Stack<MenuElement> stacks;
	private MenuSelectionManager(){
		stacks=new Stack<MenuElement>();
	}
	public static MenuSelectionManager defaultManager() {
		if(defaultManager==null)
			defaultManager=new MenuSelectionManager();
		return defaultManager;
	}
	public MenuElement[] getSelectedPath() {
		return stacks==null?null:stacks.toArray(new MenuElement[stacks.size()]);
	}
	public Stack<MenuElement> getSelectionStack(){
		return stacks;
	}
}
