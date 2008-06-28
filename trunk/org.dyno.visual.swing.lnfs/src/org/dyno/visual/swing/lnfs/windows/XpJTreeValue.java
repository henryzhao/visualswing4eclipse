package org.dyno.visual.swing.lnfs.windows;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class XpJTreeValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public XpJTreeValue() {
		put("visible", true);
		put("expandsSelectedPaths", true);
		put("toggleClickCount", 2);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("focusable", true);
		put("enabled", true);
		put("visibleRowCount", 20);
		put("scrollsOnExpand", true);
		put("rowHeight", 16);
		put("rootVisible", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
	}
}
