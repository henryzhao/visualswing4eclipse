package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.EditorAction;

public class SameWidthAction extends EditorAction {
	private static String SAME_WIDTH_ACTION_ICON = "/icons/same_width.png";

	public SameWidthAction() {
		setId(SAME_WIDTH);
		setText("Same Width");
		setToolTipText("Same Width");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(SAME_WIDTH_ACTION_ICON));
		setEnabled(false);
	}
}
