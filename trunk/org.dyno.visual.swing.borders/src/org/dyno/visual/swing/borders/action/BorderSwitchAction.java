/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.borders.action;

import javax.swing.JComponent;
import javax.swing.border.Border;

import org.dyno.visual.swing.base.IFactory;
import org.dyno.visual.swing.borders.undo.BorderSwitchOperation;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;
/**
 * 
 * BevelBorderSwitchAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class BorderSwitchAction extends Action {
	private JComponent target;
	private IFactory factory;
	private Class<? extends Border> borderClass;
	private static String getBorderName(Class<? extends Border> borderClass){
		String borderName = borderClass.getName();
		int dot = borderName.lastIndexOf(".");
		if(dot!=-1)
		borderName = borderName.substring(dot+1);
		return borderName;
	}
	public BorderSwitchAction(JComponent w, Class<? extends Border> borderClass, IFactory factory) {
		super(getBorderName(borderClass), AS_RADIO_BUTTON);
		target = w;
		this.factory = factory;
		this.borderClass = borderClass;
		setId(borderClass.getName()+"_action");
		Border border = w.getBorder();
		setChecked(border != null && border.getClass() == borderClass);
	}	
	@Override
	public void run() {
		Border border = target.getBorder();
		if (border == null || border.getClass() != borderClass) {
			IUndoableOperation operation = new BorderSwitchOperation(target, factory);
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
