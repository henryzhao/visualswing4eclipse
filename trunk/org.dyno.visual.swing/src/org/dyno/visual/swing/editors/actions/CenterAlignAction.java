package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.EditorAction;

public class CenterAlignAction extends EditorAction {
	private static String CENTER_ACTION_ICON = "/icons/center_align.png";

	public CenterAlignAction() {
		setId(ALIGNMENT_CENTER);
		setText("Center horizontally");
		setToolTipText("Center horizontally");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(CENTER_ACTION_ICON));
		setEnabled(false);
	}
}
