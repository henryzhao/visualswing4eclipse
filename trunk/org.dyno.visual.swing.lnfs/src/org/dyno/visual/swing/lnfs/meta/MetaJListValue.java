package org.dyno.visual.swing.lnfs.meta;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class MetaJListValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public MetaJListValue() {
	 	put("selectionMode", 2);
		put("model", null);
		put("visible", true);
		put("fixedCellHeight", -1);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("autoscrolls", true);
		put("enabled", true);
		put("focusable", true);
		put("selectedIndex", -1);
		put("visibleRowCount", 8);
		put("alignmentX", 0.5f);
		put("fixedCellWidth", -1);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);   		
	}
}
