package org.dyno.visual.swing.lnfs.meta;

public class MetaJSeparatorValue extends MetaJComponentValue {
	private static final long serialVersionUID = 1L;

	public MetaJSeparatorValue() {
		put("enabled", true);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("focusable", false);
		put("doubleBuffered", false);		
	}
}
