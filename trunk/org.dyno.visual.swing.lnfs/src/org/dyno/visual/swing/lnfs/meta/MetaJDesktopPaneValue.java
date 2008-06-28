package org.dyno.visual.swing.lnfs.meta;

public class MetaJDesktopPaneValue extends MetaContainerValue {
	private static final long serialVersionUID = 1L;
	public MetaJDesktopPaneValue() {
		put("focusable", true);
		put("enabled", true);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("focusCycleRoot", true);
		put("opaque", true);
	}
}
