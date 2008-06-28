package org.dyno.visual.swing.lnfs.windows;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class XpJPanelValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public XpJPanelValue() {
		put("focusable", true);
		put("enabled", true);
		put("visible", true);
		put("doubleBuffered", true);
		put("verifyInputWhenFocusTarget", true);
		put("alignmentX", 0.0f);
		put("alignmentY", 0.0f);
		put("requestFocusEnabled", true);
		put("opaque", true);
	}
}
