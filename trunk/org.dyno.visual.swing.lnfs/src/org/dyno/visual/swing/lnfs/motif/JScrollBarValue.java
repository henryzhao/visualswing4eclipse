package org.dyno.visual.swing.lnfs.motif;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JScrollBarValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JScrollBarValue() {
		put("blockIncrement", 10);
		put("maximum", 100);
		put("orientation", 1);
		put("unitIncrement", 1);
		put("visibleAmount", 10);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);		
	}
}
