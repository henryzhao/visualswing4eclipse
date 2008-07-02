package org.dyno.visual.swing.lnfs.nimbus;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JFormattedTextFieldValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JFormattedTextFieldValue() {
		put("editable", true);
		put("horizontalAlignment", 10);
		put("focusLostBehavior", 1);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", false);
		put("autoscrolls", false);
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
	}
}
