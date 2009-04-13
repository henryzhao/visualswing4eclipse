package org.dyno.visual.swing.types.endec;

import org.dyno.visual.swing.plugin.spi.IEndec;

public class BooleanEndec implements IEndec {

	
	public Object decode(String string) {
		return string!=null&&string.equals("true");
	}

	
	public String encode(Object value) {
		return value==null?"false":value.toString();
	}

}
