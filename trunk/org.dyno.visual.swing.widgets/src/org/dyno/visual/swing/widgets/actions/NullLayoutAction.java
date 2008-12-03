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

