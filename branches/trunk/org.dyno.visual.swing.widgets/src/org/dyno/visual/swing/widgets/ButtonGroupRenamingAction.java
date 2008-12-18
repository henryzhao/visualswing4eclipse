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

package org.dyno.visual.swing.widgets;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.undo.ButtonGroupRenamingOperation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

public class ButtonGroupRenamingAction extends Action {
	private WidgetAdapter adapter;
	private ButtonGroupAdapter group;
	public ButtonGroupRenamingAction(WidgetAdapter adapter, ButtonGroupAdapter group) {
		this.adapter = adapter;
		setText("Change Variable Name ...");
		setId("change_variable_name");
		setToolTipText("Change Variable Name ...");
		this.group = group;
	}

	public void run() {
		IUndoableOperation operation = new ButtonGroupRenamingOperation(adapter, group);
		operation.addContext(adapter.getUndoContext());
		IOperationHistory history = PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
		try {
			history.execute(operation, null, null);
		} catch (ExecutionException e) {
			WidgetPlugin.getLogger().error(e);
		}
	}
}

