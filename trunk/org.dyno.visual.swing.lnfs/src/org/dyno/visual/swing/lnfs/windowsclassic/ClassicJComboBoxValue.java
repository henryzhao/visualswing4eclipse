package org.dyno.visual.swing.lnfs.windowsclassic;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class ClassicJComboBoxValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public ClassicJComboBoxValue() {
		put("maximumRowCount", 8);
		put("model", null);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("enabled", true);
		put("focusable", true);
		put("lightWeightPopupEnabled", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
	}
}
