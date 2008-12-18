package org.dyno.visual.swing.types.endec;

import org.dyno.visual.swing.plugin.spi.IEndec;

public class FloatEndec implements IEndec {

	@Override
	public Object decode(String string) {
		return string==null?0.0f:Float.parseFloat(string);
	}

	@Override
	public String encode(Object value) {
		return value==null?"0.0":value.toString();
	}

}
