package org.dyno.visual.swing.lnfs.meta;


public class MetaJTextAreaValue extends MetaJComponentValue {
	private static final long serialVersionUID = 1L;
	public MetaJTextAreaValue() {
		put("editable", true);
		put("tabSize", 8);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("opaque", true);
		put("autoscrolls", true);
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("doubleBuffered", false);
	}
}
