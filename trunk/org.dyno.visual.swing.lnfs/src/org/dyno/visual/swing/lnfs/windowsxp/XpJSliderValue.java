package org.dyno.visual.swing.lnfs.windowsxp;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class XpJSliderValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public XpJSliderValue() {
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
	}
}
