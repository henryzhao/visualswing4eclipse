package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.plugin.spi.EditorAction;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

public class CutAction extends EditorAction {
	public CutAction() {
		setId(ActionFactory.CUT.getId());
		setToolTipText("Cut Components (Ctrl+X)");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
		setAccelerator(SWT.CTRL | 'X');
		setRetargetable(true);
		setEnabled(false);
	}

	@Override
	public ActionFactory getActionFactory() {
		return ActionFactory.CUT;
	}

}
