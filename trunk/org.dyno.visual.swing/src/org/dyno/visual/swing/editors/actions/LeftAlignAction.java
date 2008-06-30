package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.EditorAction;

public class LeftAlignAction extends EditorAction {
	private static String LEFT_ACTION_ICON = "/icons/left_align.png";

	public LeftAlignAction() {
		setId(ALIGNMENT_LEFT);
		setText("Left Alignment in Column");
		setToolTipText("Left Alignment in Column");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(LEFT_ACTION_ICON));
		setEnabled(false);
	}
}
