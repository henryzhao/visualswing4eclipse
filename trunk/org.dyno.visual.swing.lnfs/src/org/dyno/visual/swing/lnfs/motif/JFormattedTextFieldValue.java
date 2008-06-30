package org.dyno.visual.swing.lnfs.motif;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JFormattedTextFieldValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JFormattedTextFieldValue() {
		put("editable", true);
		put("horizontalAlignment", 10);
		put("focusLostBehavior", 1);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("autoscrolls", true);
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
	}
}
