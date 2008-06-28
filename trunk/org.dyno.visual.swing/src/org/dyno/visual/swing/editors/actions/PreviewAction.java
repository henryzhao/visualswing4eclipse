package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.EditorAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

public class PreviewAction extends EditorAction {
	private static String PREVIEW_ACTION_ICON = "/icons/preview.png";

	public PreviewAction() {
		setId(PREVIEW);
		setText("Preview Design");
		setToolTipText("Preview Design");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(PREVIEW_ACTION_ICON));
	}

	@Override
	public void addToMenu(IMenuManager editMenu) {
		editMenu.insertAfter(ITextEditorActionConstants.SELECT_ALL, this);
	}
}
