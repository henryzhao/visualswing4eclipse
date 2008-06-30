package org.dyno.visual.swing.lnfs.windowsclassic;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class XpJFormattedTextFieldValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public XpJFormattedTextFieldValue() {
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
