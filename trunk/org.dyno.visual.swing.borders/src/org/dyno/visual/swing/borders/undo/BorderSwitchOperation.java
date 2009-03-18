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

package org.dyno.visual.swing.borders.undo;

import javax.swing.JComponent;
import javax.swing.border.Border;

import org.dyno.visual.swing.base.IFactory;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class BorderSwitchOperation extends AbstractOperation {
	private JComponent target;
	private WidgetAdapter targetAdapter;
	private Border prevousBorder;
	private IFactory factory;
	public BorderSwitchOperation(JComponent w, IFactory factory) {
		super(Messages.BorderSwitchOperation_Switch_Border);
		this.target = w;
		this.targetAdapter = WidgetAdapter.getWidgetAdapter(target);
		this.factory = factory;
		this.prevousBorder = target.getBorder();
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		target.setBorder((Border)factory.newInstance(target));
		target.repaint();
		targetAdapter.setDirty(true);
		targetAdapter.addNotify();
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		target.setBorder(prevousBorder);
		target.repaint();
		targetAdapter.setDirty(true);
		targetAdapter.addNotify();
		return Status.OK_STATUS;
	}
}

