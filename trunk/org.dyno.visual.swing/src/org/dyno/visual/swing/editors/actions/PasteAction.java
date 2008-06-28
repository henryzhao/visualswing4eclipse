package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.plugin.spi.EditorAction;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

public class PasteAction extends EditorAction {
	public PasteAction() {
		setId(ActionFactory.PASTE.getId());
		setToolTipText("Paste Components (Ctrl+V)");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		setAccelerator(SWT.CTRL | 'V');
		setRetargetable(true);
		setEnabled(false);
	}

	@Override
	public ActionFactory getActionFactory() {
		return ActionFactory.PASTE;
	}

}
