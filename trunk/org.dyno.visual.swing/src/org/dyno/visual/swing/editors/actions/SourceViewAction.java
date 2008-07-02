package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.EditorAction;
import org.eclipse.jface.action.IMenuManager;

public class SourceViewAction extends EditorAction {
	private static String SOURCE_ACTION_ID = "/icons/source_view.png";

	public SourceViewAction() {
		setId(SOURCE);
		setText("View Source Code");
		setToolTipText("View Source Code");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(SOURCE_ACTION_ID));
	}

	@Override
	public void run() {
		if (editor == null)
			return;
		editor.openSourceEditor();
	}

	@Override
	public void addToMenu(IMenuManager editMenu) {
		editMenu.insertAfter(PREVIEW, this);
	}
}
