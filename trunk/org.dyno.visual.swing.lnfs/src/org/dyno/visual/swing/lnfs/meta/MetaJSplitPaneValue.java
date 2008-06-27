package org.dyno.visual.swing.lnfs.meta;

import javax.swing.JSplitPane;

public class MetaJSplitPaneValue extends MetaJComponentValue {
	private static final long serialVersionUID = 1L;

	public MetaJSplitPaneValue() {
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("doubleBuffered", false);
		
		put("dividerLocation", 59);
		put("dividerSize", 10);
		put("orientation", JSplitPane.HORIZONTAL_SPLIT);
		put("verifyInputWhenFocusTarget", true);
		put("lastDividerLocation", -1);
		put("alignmentX", 0.0f);
		put("alignmentY", 0.0f);
		
		
	}
}
