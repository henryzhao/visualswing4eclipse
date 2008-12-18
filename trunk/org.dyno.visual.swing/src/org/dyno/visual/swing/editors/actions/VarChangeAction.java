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

package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
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
			VisualSwingPlugin.getLogger().error(e);
		}
	}
}

