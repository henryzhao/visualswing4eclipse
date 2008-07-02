package org.dyno.visual.swing.lnfs.windowsclassic;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class ClassicJTabbedPaneValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public ClassicJTabbedPaneValue() {
		put("tabPlacement", 1);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
	}
}
