package org.dyno.visual.swing.lnfs.motif;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JTabbedPaneValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JTabbedPaneValue() {
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
