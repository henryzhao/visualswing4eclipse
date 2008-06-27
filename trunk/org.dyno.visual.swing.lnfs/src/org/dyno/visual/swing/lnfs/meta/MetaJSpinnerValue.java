package org.dyno.visual.swing.lnfs.meta;

import javax.swing.SpinnerNumberModel;

public class MetaJSpinnerValue extends MetaContainerValue {
	private static final long serialVersionUID = 1L;
	public MetaJSpinnerValue() {
		put("focusable", true);
		put("enabled", true);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("requestFocusEnabled", true);
		put("opaque", true);
		put("model", new SpinnerNumberModel());
	}
}
