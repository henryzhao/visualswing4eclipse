package org.dyno.visual.swing.designer;

import java.util.HashMap;
import java.util.Map;

import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class VSNamespaceManager implements NamespaceManager{
	private WidgetAdapter rootAdapter;
	private Map<String, String>names;
	public VSNamespaceManager(WidgetAdapter rootAdapter){
		this.rootAdapter = rootAdapter;
		names = new HashMap<String, String>();
	}
	@Override
	public boolean hasDeclaredName(String newName) {
		String name = names.get(newName);
		if(name!=null)
			return true;		
		return rootAdapter.includeName(newName);
	}
	@Override
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
