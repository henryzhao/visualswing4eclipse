package org.dyno.visual.swing.lnfs.windowsclassic;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class ClassicJToggleButtonValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public ClassicJToggleButtonValue() {
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
		put("opaque", true);
	}
}
