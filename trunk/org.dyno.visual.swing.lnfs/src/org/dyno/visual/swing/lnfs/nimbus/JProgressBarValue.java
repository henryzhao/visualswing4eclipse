package org.dyno.visual.swing.lnfs.nimbus;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JProgressBarValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JProgressBarValue() {
		put("maximum", 100);
		put("visible", true);
		put("borderPainted", true);
		put("verifyInputWhenFocusTarget", true);
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("string","0%");
		put("opaque", false);
	}
}