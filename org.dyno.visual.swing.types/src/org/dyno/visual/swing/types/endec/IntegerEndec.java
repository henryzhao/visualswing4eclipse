package org.dyno.visual.swing.types.endec;

import org.dyno.visual.swing.plugin.spi.IEndec;

public class IntegerEndec implements IEndec {

	
	public Object decode(String string) {
		if(string==null)
			return 0;
		return Integer.parseInt(string);
	}

	
	public String encode(Object value) {
		return value==null?"0":value.toString();
	}

}
