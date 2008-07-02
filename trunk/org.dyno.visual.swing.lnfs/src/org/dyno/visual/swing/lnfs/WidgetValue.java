package org.dyno.visual.swing.lnfs;

import java.util.HashMap;

public abstract class WidgetValue extends HashMap<String, Object>{
	private static final long serialVersionUID = 1L;

	public Object getDefaultValue(String propertyName){
		return get(propertyName);
	}
}
