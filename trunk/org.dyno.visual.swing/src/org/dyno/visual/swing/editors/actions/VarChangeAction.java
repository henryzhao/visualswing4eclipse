package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.undo.VarChangeOperation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

public class VarChangeAction extends Action {
	private WidgetAdapter adapter;

	public VarChangeAction(WidgetAdapter adapter) {
		this.adapter = adapter;
		setText("Change Variable Name ...");
		setId("change_variable_name");
		setToolTipText("Change Variable Name ...");
	}

	public void run() {
		IUndoableOperation operation = new VarChangeOperation(adapter);
		operation.addContext(adapter.getUndoContext());
		IOperationHistory history = PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
		try {
			history.execute(operation, null, null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
