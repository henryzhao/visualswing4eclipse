package org.dyno.visual.swing.lnfs.nimbus;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JScrollPaneValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JScrollPaneValue() {
		put("horizontalScrollBarPolicy", 30);
		put("verticalScrollBarPolicy", 20);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", false);
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("wheelScrollingEnabled", true);
	}
}
