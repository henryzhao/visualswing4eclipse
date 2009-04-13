package org.dyno.visual.swing.designer;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

@SuppressWarnings("unchecked")
public class VSNamespaceManager implements NamespaceManager{
	private WidgetAdapter rootAdapter;
	private Map<String, String>names;
	public VSNamespaceManager(WidgetAdapter rootAdapter){
		this.rootAdapter = rootAdapter;
		names = new HashMap<String, String>();
	}
	
	public boolean hasDeclaredName(String newName) {
		String name = names.get(newName);
		if(name!=null)
			return true;
		Component widget = rootAdapter.getWidget();
		Class clazz;
		if(widget!=null)
			clazz=widget.getClass();
		else
			clazz=rootAdapter.getWidgetClass();
		String getMethodName = "get" + Character.toUpperCase(newName.charAt(0)) + newName.substring(1);
		if(hasMethod(clazz, getMethodName))
			return true;
		return rootAdapter.includeName(newName);
	}
	
	private boolean hasMethod(Class clazz, String getMethodName) {
		try {
			clazz.getDeclaredMethod(getMethodName);
			return true;
		} catch (Exception e) {
		}
		if (clazz == Object.class)
			return false;
		return hasMethod(clazz.getSuperclass(), getMethodName);
	}
	
	public String nextName(String base) {
		for(int i=0;i<Integer.MAX_VALUE;i++){
			if(!hasDeclaredName(base+i))
				return base+i;
		}
		return null;
	}
	public void addName(String elementName) {
		if(!hasDeclaredName(elementName)){
			names.put(elementName, elementName);
		}
	}
}
