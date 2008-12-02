/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
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

public class MoveResizeOperation extends AbstractOperation {
	private CompositeAdapter old_parent;
	private CompositeAdapter new_parent;
	private List<Object> old_constraints;
	private List<Object> new_constraints;
	private List<Component> children;

	public MoveResizeOperation(CompositeAdapter old_parent, CompositeAdapter new_parent, List<Component> children, List<Object> old_constraints, List<Object> new_constraints) {
		super("Reshape Component");
		this.old_parent = old_parent;
		this.new_parent = new_parent;
		this.old_constraints = old_constraints;
		this.new_constraints = new_constraints;
		this.children = children;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		for (int i = 0; i < children.size(); i++) {
			Component child = children.get(i);
			Object new_constraint = new_constraints.get(i);
			old_parent.removeChild(child);
			new_parent.addChildByConstraints(child, new_constraint);
		}
		old_parent.getWidget().validate();
		new_parent.getWidget().validate();
		new_parent.repaintDesigner();
		old_parent.setDirty(true);
		new_parent.setDirty(true);
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		for (int i = 0; i < children.size(); i++) {
			Component child = children.get(i);
			Object old_constraint = old_constraints.get(i);
			new_parent.removeChild(child);
			old_parent.addChildByConstraints(child, old_constraint);
		}
		new_parent.getWidget().validate();
		old_parent.getWidget().validate();
		old_parent.repaintDesigner();
		new_parent.setDirty(true);
		old_parent.setDirty(true);
		return Status.OK_STATUS;
	}
}
