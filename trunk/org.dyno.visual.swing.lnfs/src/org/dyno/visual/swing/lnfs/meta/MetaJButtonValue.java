package org.dyno.visual.swing.lnfs.meta;

public class MetaJButtonValue extends MetaAbstractButtonValue {
	private static final long serialVersionUID = 1L;

	public MetaJButtonValue() {
		put("opaque", true);
        put("defaultCapable", true);
		put("doubleBuffered", false);
		put("alignmentY", 0.5f);		
	}

}
