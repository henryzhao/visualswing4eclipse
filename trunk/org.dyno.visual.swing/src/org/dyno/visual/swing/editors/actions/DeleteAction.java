package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.plugin.spi.EditorAction;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

public class DeleteAction extends EditorAction {
	public DeleteAction() {
		setId(ActionFactory.DELETE.getId());
		setToolTipText("Delete Components (Delete)");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		setAccelerator(SWT.DEL);
		setRetargetable(true);
		setEnabled(false);
	}

	@Override
	public ActionFactory getActionFactory() {
		return ActionFactory.DELETE;
	}

}
