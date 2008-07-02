package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.EditorAction;

public class SameHeightAction extends EditorAction {
	private static String SAME_HEIGHT_ACTION_ICON = "/icons/same_height.png";

	public SameHeightAction() {
		setId(SAME_HEIGHT);
		setText("Same Height");
		setToolTipText("Same Height");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(SAME_HEIGHT_ACTION_ICON));
		setEnabled(false);
	}
}
