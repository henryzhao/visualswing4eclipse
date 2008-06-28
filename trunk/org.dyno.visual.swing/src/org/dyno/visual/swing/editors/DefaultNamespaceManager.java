package org.dyno.visual.swing.editors;

import org.dyno.visual.swing.base.NamespaceManager;

public class DefaultNamespaceManager extends NamespaceManager{
	private static String GET = "get";
	private static String PREFIX = "$";
	public String getGetMethodName(String fieldName) {
		return GET + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}
	public boolean isDesigningField(String name){
		return name.startsWith(PREFIX);
	}
	public String getNameFromFieldName(String name){
		return name.substring(PREFIX.length());
	}
	public String getFieldName(String name){
		return PREFIX+name;
	}
}
