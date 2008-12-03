
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

package org.dyno.visual.swing.borders.action;

import javax.swing.JComponent;
import javax.swing.border.Border;

import org.dyno.visual.swing.borders.undo.NullBorderSwitchOperation;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

public class NullBorderSwitchAction extends Action {
	private JComponent target;

	public NullBorderSwitchAction(JComponent w) {
		super("Null Border", AS_RADIO_BUTTON);
		target = w;
		setId("null_border_switch_action");
		Border border = w.getBorder();
		setChecked(border == null);
	}

	@Override
	public void run() {
		if (target.getBorder() != null) {
			IUndoableOperation operation = new NullBorderSwitchOperation(target);
			WidgetAdapter targetAdapter = WidgetAdapter.getWidgetAdapter(target);
			operation.addContext(targetAdapter.getUndoContext());
			IOperationHistory history = PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
			try {
				history.execute(operation, null, null);
				setChecked(true);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
}

