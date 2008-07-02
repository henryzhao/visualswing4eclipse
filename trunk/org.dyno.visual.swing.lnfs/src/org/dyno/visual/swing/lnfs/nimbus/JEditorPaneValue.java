package org.dyno.visual.swing.lnfs.nimbus;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JEditorPaneValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public JEditorPaneValue() {
		put("contentType", "text/plain");
		put("editable", true);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("autoscrolls", false);
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("focusCycleRoot", true);
		put("requestFocusEnabled", true);
	}
}
