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

package org.dyno.visual.swing.designer;

import java.awt.Component;
import java.util.List;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

class DragDropOperation extends AbstractOperation {
	private CompositeAdapter parent;
	private List<Object> constraints;
	private List<Component> children;

	public DragDropOperation(CompositeAdapter parent, List<Component> children, List<Object> constraints) {
		super("Delete Component");
		this.parent = parent;
		this.constraints = constraints;
		this.children = children;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		for (Component child : children) {
			parent.removeChild(child);
		}
		parent.getWidget().validate();
		parent.repaintDesigner();
		parent.setDirty(true);
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		for (int i = 0; i < children.size(); i++) {
			Component child = children.get(i);
			Object constraint = constraints.get(i);
			parent.addChildByConstraints(child, constraint);
		}
		parent.getWidget().validate();
		parent.repaintDesigner();
		parent.setDirty(true);
		return Status.OK_STATUS;
	}
}

