package org.dyno.visual.swing.plugin.spi;

public interface IEndec {
	Object decode(String string);
	String encode(Object value);
}
