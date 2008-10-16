package org.dyno.visual.swing.lnfs.windowsclassic;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class ClassicJMenuItemValue extends WidgetValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClassicJMenuItemValue() {
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("horizontalAlignment", 10);
		put("opaque", true);
		put("contentAreaFilled", true);
		put("enabled", true);
		put("iconTextGap", 4);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
	}

}
