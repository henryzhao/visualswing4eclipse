package org.dyno.visual.swing.types.endec;

import org.dyno.visual.swing.plugin.spi.IEndec;

public class StringEndec implements IEndec {

	
	public Object decode(String string) {
		return string;
	}

	
	public String encode(Object value) {
		return value==null?"null":value.toString();
	}

}
