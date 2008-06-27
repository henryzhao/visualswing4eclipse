package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.EditorAction;

public class MiddleAlignAction extends EditorAction {
	private static String MIDDLE_ACTION_ICON = "/icons/middle_align.png";

	public MiddleAlignAction() {
		setId(ALIGNMENT_MIDDLE);
		setText("Center vertically");
		setToolTipText("Center vertically");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(MIDDLE_ACTION_ICON));
		setEnabled(false);
	}
}
