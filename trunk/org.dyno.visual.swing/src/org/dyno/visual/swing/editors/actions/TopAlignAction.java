package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.EditorAction;

public class TopAlignAction extends EditorAction {
	private static String TOP_ACTION_ICON = "/icons/top_align.png";

	public TopAlignAction() {
		setId(ALIGNMENT_TOP);
		setText("Top Alignment in Row");
		setToolTipText("Top Alignment in Row");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(TOP_ACTION_ICON));
		setEnabled(false);
	}
}
