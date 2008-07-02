package org.dyno.visual.swing.lnfs.motif;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JToggleButtonValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JToggleButtonValue() {
		put("visible", true);
		put("borderPainted", true);
		put("verifyInputWhenFocusTarget", true);
		put("focusPainted", true);
		put("contentAreaFilled", true);
		put("enabled", true);
		put("focusable", true);
		put("iconTextGap", 4);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("rolloverEnabled", false);
	}
}
