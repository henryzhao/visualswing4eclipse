package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.EditorAction;

public class BottomAlignAction extends EditorAction {
	private static String BOTTOM_ACTION_ICON = "/icons/bottom_align.png";

	public BottomAlignAction() {
		setId(ALIGNMENT_BOTTOM);
		setText("Bottom Alignment in Row");
		setToolTipText("Bottom Alignment in Row");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(BOTTOM_ACTION_ICON));
		setEnabled(false);
	}
}
