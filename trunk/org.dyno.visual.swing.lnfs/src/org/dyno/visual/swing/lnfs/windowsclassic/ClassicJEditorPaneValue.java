package org.dyno.visual.swing.lnfs.windowsclassic;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class ClassicJEditorPaneValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public ClassicJEditorPaneValue() {
		put("contentType", "text/plain");
		put("editable", true);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("autoscrolls", true);
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("focusCycleRoot", true);
		put("requestFocusEnabled", true);
	}
}
