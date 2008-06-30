package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.EditorAction;
import org.eclipse.swt.SWT;
import org.eclipse.ui.actions.ActionFactory;

public class SelectAllAction extends EditorAction {
	private static String SELECT_ALL_ICON = "/icons/selall.png";

	public SelectAllAction() {
		setId(ActionFactory.SELECT_ALL.getId());
		setToolTipText("Select All (Ctrl+A)");
		setAccelerator(SWT.CTRL | 'A');
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(SELECT_ALL_ICON));
		setRetargetable(true);
		setEnabled(false);
	}

	@Override
	public ActionFactory getActionFactory() {
		return ActionFactory.SELECT_ALL;
	}

}
