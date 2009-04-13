package org.dyno.visual.swing.types.endec;

import org.dyno.visual.swing.plugin.spi.IEndec;

public class CharEndec implements IEndec{

	
	public Object decode(String string) {
		return string==null?'\0':(string.length()==0?'\0':string.charAt(0));
	}

	
	public String encode(Object value) {
		return value==null?"null":value.toString();
	}

}
