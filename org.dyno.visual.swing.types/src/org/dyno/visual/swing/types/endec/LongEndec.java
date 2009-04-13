package org.dyno.visual.swing.types.endec;

import org.dyno.visual.swing.plugin.spi.IEndec;

public class LongEndec implements IEndec {

	
	public Object decode(String string) {
		return string==null?0l:Long.parseLong(string);
	}

	
	public String encode(Object value) {
		return value==null?"0":value.toString();
	}

}
