package org.dyno.visual.swing.widgets.actions;

import javax.swing.JPanel;

import org.dyno.visual.swing.widgets.JPanelAdapter;
import org.dyno.visual.swing.widgets.undo.NullLayoutOperation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

public class NullLayoutAction extends Action {
	private JPanelAdapter adapter;

	public NullLayoutAction(JPanelAdapter adapter) {
		super("Null Layout", AS_RADIO_BUTTON);
		this.adapter = adapter;
	}

	public void run() {
		JPanel jpanel = (JPanel) adapter.getWidget();
		if (jpanel.getLayout() != null) {
			IUndoableOperation operation = new NullLayoutOperation(adapter);
			operation.addContext(adapter.getUndoContext());
			IOperationHistory operationHistory = PlatformUI.getWorkbench()
					.getOperationSupport().getOperationHistory();
			try {
				operationHistory.execute(operation, null, null);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
}
