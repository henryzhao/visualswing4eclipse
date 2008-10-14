package org.dyno.visual.swing.base;

import javax.swing.MenuElement;

public class MenuSelectionManager {
	private static MenuSelectionManager defaultManager;
	private MenuSelectionManager(){
	}
	public static MenuSelectionManager defaultManager() {
		if(defaultManager==null)
			defaultManager=new MenuSelectionManager();
		return defaultManager;
	}
	public MenuElement[] getSelectedPath() {
		return null;
	}
}
