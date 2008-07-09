package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.adapter.BeanNameValidator;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ICellEditorValidator;

public class VarChangeAction extends Action {
	private WidgetAdapter adapter;
	private ICellEditorValidator validator;

	public VarChangeAction(WidgetAdapter adapter) {
		this.adapter = adapter;
		this.validator = new BeanNameValidator(adapter);
		setText("Change Variable Name ...");
		setId("change_variable_name");
		setToolTipText("Change Variable Name ...");
	}

	public void run() {
		while (true) {
			VarNameDialog dialog = new VarNameDialog(adapter.getShell());
			dialog
					.setPromptMessage("Please enter a new variable name for this component:");
			dialog.setInput(adapter.getName());
			if (dialog.open() == Dialog.OK) {
				String name = dialog.getInput();
				String info = validator.isValid(name);
				if (info != null) {
					MessageDialog.openError(adapter.getShell(),
							"Invalid identifier", info);
				} else {
					adapter.setName(name);
					if (!adapter.isRoot()) {
						adapter.getParentAdapter().setDirty(true);
					}
					adapter.setDirty(true);
					adapter.changeNotify();
					break;
				}
			} else
				break;
		}
	}
}
