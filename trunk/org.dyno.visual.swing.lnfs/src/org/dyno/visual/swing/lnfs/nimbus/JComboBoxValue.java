package org.dyno.visual.swing.lnfs.nimbus;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JComboBoxValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JComboBoxValue() {
		put("maximumRowCount", 8);
		put("model", null);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", false);
		put("enabled", true);
		put("focusable", true);
		put("lightWeightPopupEnabled", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
	}
}