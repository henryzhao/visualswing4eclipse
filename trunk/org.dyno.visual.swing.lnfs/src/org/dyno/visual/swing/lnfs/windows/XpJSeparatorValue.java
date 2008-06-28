package org.dyno.visual.swing.lnfs.windows;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class XpJSeparatorValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public XpJSeparatorValue() {
		put("enabled", true);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);		
	}
}
