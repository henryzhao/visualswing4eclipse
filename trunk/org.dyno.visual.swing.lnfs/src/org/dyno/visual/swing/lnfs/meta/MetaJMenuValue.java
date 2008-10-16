package org.dyno.visual.swing.lnfs.meta;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class MetaJMenuValue extends WidgetValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetaJMenuValue() {
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("borderPainted", true);
		put("horizontalAlignment", 10);
		put("contentAreaFilled", true);
		put("focusable", true);
		put("enabled", true);
		put("iconTextGap", 4);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
	}
}
