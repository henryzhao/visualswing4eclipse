package org.dyno.visual.swing.base;

import org.dyno.visual.swing.editors.DefaultNamespaceManager;

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
	
	public abstract boolean isDesigningField(String fieldName);
	
	public abstract String getNameFromFieldName(String fieldName);
	
	public abstract String getFieldName(String name);
	
	public abstract String getGetMethodName(String name);
}
