package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.plugin.spi.EditorAction;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

public class CopyAction extends EditorAction {
	public CopyAction() {
		setId(ActionFactory.COPY.getId());
		setToolTipText("Copy Components (Ctrl+C)");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
		setAccelerator(SWT.CTRL | 'C');
		setRetargetable(true);
		setEnabled(false);
	}

	@Override
	public ActionFactory getActionFactory() {
		return ActionFactory.COPY;
	}
}
