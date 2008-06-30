package org.dyno.visual.swing.lnfs.nimbus;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JButtonValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JButtonValue() {
		put("visible", true);
		put("borderPainted", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", false);
		put("eocusPainted", true);
		put("contentAreaFilled", true);
		put("enabled", true);
		put("focusable", true);
		put("iconTextGap", 4);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("rolloverEnabled", true);
		put("focusPainted", true);
	}
}
