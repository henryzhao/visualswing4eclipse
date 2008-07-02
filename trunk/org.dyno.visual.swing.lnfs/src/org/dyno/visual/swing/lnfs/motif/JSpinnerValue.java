package org.dyno.visual.swing.lnfs.motif;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JSpinnerValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JSpinnerValue() {
		put("focusable", true);
		put("enabled", true);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("opaque", true);		
	}
}
