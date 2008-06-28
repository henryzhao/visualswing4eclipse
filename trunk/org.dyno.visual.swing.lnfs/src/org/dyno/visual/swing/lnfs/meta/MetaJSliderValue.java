package org.dyno.visual.swing.lnfs.meta;

public class MetaJSliderValue extends MetaJComponentValue {
	private static final long serialVersionUID = 1L;

	public MetaJSliderValue() {
		put("maximum", 100);
		put("paintTrack", true);
		put("value", 50);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("focusable", true);
		put("enabled", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("doubleBuffered", false);
	}
}
