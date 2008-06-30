package org.dyno.visual.swing.lnfs.meta;

public class MetaJToggleButtonValue extends MetaAbstractButtonValue {
	private static final long serialVersionUID = 1L;

	public MetaJToggleButtonValue() {
		put("opaque", true);
        put("defaultCapable", true);
		put("doubleBuffered", false);
		put("alignmentY", 0.5f);
		put("rolloverEnabled", false);
	}

}
