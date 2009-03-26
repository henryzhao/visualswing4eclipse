/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.widgets.layout.actions;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.layout.LayoutPlugin;
import org.dyno.visual.swing.widgets.layout.undo.BorderLayoutPlacementOperation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

public class BorderLayoutPlacementAction extends Action {
	private String placement;
	private JComponent child;
	private JComponent container;
	private CompositeAdapter parent;

	public BorderLayoutPlacementAction(JComponent container, String placement,
			JComponent child) {
		super("", AS_RADIO_BUTTON);
		this.container = container;
		this.placement = placement;
		this.child = child;
		this.parent = (CompositeAdapter) WidgetAdapter
				.getWidgetAdapter(container);
		setId("#BORDERLAYOUT_CONSTRAINTS_" + placement);
		setText(placement);
		setChecked(placement.equals(parent.getChildConstraints(child)));
	}

	public void run() {
		String oldplacement = (String) parent.getChildConstraints(child);
		if (!placement.equals(oldplacement)) {
			IOperationHistory operationHistory = PlatformUI.getWorkbench()
					.getOperationSupport().getOperationHistory();
			IUndoableOperation operation = new BorderLayoutPlacementOperation(
					container, child, placement);
			operation.addContext(parent.getUndoContext());
			try {
				operationHistory.execute(operation, null, null);
			} catch (ExecutionException e) {
				LayoutPlugin.getLogger().error(e);
			}
		}
	}
}

