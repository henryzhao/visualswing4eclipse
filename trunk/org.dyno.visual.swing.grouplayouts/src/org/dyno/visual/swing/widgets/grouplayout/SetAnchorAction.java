package org.dyno.visual.swing.widgets.grouplayout;

import javax.swing.JComponent;

import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.grouplayout.undo.HorizontalLeadingToBilateralOperation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

class SetAnchorAction extends Action {
	private JComponent container;
	private String anchor;
	private boolean horizontal;
	private JComponent child;

	public SetAnchorAction(JComponent container, boolean horizontal,
			String anchor, JComponent child) {
		super("", AS_RADIO_BUTTON);
		setId("#ANCHOR_" + (horizontal ? "HORIZONTAL" : "VERTICAL") + "_"
				+ anchor);
		setText(anchor);
		this.container = container;
		this.horizontal = horizontal;
		this.anchor = anchor;
		this.child = child;
		setChecked(shouldChecked());
	}

	private boolean shouldChecked() {
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter
				.getWidgetAdapter(container);
		Constraints constraints = (Constraints) containerAdapter
				.getChildConstraints(child);
		Alignment alignment;
		if (horizontal) {
			alignment = constraints.getHorizontal();
		} else {
			alignment = constraints.getVertical();
		}
		String oldanchor = "leading";
		if (alignment instanceof Leading) {
			oldanchor = "leading";
		} else if (alignment instanceof Bilateral) {
			oldanchor = "bilateral";
		} else if (alignment instanceof Trailing) {
			oldanchor = "trailing";
		}
		return oldanchor.equals(anchor);
	}

	public void run() {
		if (!shouldChecked()) {
			IUndoableOperation operation = null;
			CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter
					.getWidgetAdapter(container);
			Constraints constraints = (Constraints) containerAdapter
					.getChildConstraints(child);
			if (horizontal) {
				Alignment alignment = constraints.getHorizontal();
				if (alignment instanceof Leading) {
					if (anchor.equals("bilateral")) {
						operation = new HorizontalLeadingToBilateralOperation(
								constraints, container, child);
					} else if (anchor.equals("trailing")) {

					}
				} else if (alignment instanceof Bilateral) {
					if (anchor.equals("leading")) {

					} else if (anchor.equals("trailing")) {

					}
				} else if (alignment instanceof Trailing) {
					if (anchor.equals("leading")) {

					} else if (anchor.equals("bilateral")) {

					}
				}
			} else {

			}
			if (operation != null) {
				WidgetAdapter adapter = WidgetAdapter
						.getWidgetAdapter(container);
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
}
