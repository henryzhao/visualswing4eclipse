package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.EditorAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

public class DuplicateAction extends EditorAction {
	private static String DUPLICATE_ACTION_ICON = "/icons/duplicate.png";

	public DuplicateAction() {
		setId(DUPLICATE);
		setText("Duplicate Components");
		setToolTipText("Duplicate Components");
		setAccelerator(SWT.CTRL | 'D');
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(DUPLICATE_ACTION_ICON));
		setEnabled(false);
	}

	@Override
	public void addToMenu(IMenuManager editMenu) {
		editMenu.insertAfter(ITextEditorActionConstants.PASTE, this);
	}
}
