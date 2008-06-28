package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.EditorAction;

public class RightAlignAction extends EditorAction {
	private static String RIGHT_ACTION_ICON = "/icons/right_align.png";

	public RightAlignAction() {
		setId(ALIGNMENT_RIGHT);
		setText("Right Alignment in Column");
		setToolTipText("Right Alignment in Column");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(RIGHT_ACTION_ICON));
		setEnabled(false);
	}
}
