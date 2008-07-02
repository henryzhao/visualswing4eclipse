package org.dyno.visual.swing.lnfs.motif;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JSeparatorValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JSeparatorValue() {
		put("enabled", true);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);		
	}
}
