package org.dyno.visual.swing.lnfs.nimbus;

import java.awt.Insets;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JMenuBarValue extends WidgetValue {
	private static final long serialVersionUID = 1L;

	public JMenuBarValue() {
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("borderPainted", true);
		put("margin", new Insets(0, 0, 0, 0));
		put("enabled", true);
		put("focusable", true);
		put("alignmentX", 0.5f);
		put("requestFocusEnabled", true);
	}

}
