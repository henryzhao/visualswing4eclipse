package org.dyno.visual.swing.types.endec;

import org.dyno.visual.swing.plugin.spi.IEndec;

public class DoubleEndec implements IEndec {

	
	public Object decode(String string) {
		return string==null?0:Double.parseDouble(string);
	}

	
	public String encode(Object value) {
		return value==null?"0.0":value.toString();
	}

}
