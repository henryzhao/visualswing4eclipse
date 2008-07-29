package org.dyno.visual.swing.widgets.actions;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.JSplitPaneAdapter;
import org.dyno.visual.swing.widgets.undo.JSplitPanePlacementOperation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

public class JSplitPanePlacementAction extends Action {
	private JSplitPane container;
	private JComponent child;
	private JSplitPaneAdapter parent;
	private String placement;

	public JSplitPanePlacementAction(JSplitPane container,
			String placement, JComponent child) {
		super("", AS_RADIO_BUTTON);
		this.container = container;
		this.placement = placement;
		this.child = child;
		this.parent = (JSplitPaneAdapter) WidgetAdapter
				.getWidgetAdapter(container);
		setId("#JSPLITPANE_CONSTRAINTS_" + placement);
		setText(placement);
		setChecked(placement.equals(parent.getChildConstraints(child)));
	}
	public void run(){
		String oldplacement = (String) parent.getChildConstraints(child);
		if (!placement.equals(oldplacement)) {
			IOperationHistory operationHistory = PlatformUI.getWorkbench()
					.getOperationSupport().getOperationHistory();
			IUndoableOperation operation = new JSplitPanePlacementOperation(container, child, placement);
			operation.addContext(parent.getUndoContext());
			try {
				operationHistory.execute(operation, null, null);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
}