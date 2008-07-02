package org.dyno.visual.swing.lnfs.windowsclassic;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class ClassicJSeparatorValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public ClassicJSeparatorValue() {
		put("enabled", true);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);		
	}
}
