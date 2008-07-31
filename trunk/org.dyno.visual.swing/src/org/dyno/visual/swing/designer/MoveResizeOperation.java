package org.dyno.visual.swing.designer;

import java.awt.Component;

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
	private Object old_constraints;
	private Object new_constraints;
	private Component child;

	public MoveResizeOperation(CompositeAdapter old_parent, CompositeAdapter new_parent, Component child, Object old_constraints, Object new_constraints) {
		super("Reshape Component");
		this.old_parent = old_parent;
		this.new_parent = new_parent;
		this.old_constraints = old_constraints;
		this.new_constraints = new_constraints;
		this.child = child;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {		
		old_parent.removeChild(child);
		new_parent.addChildByConstraints(child, new_constraints);
		old_parent.getWidget().validate();
		new_parent.getWidget().validate();
		new_parent.repaintDesigner();
		old_parent.setDirty(true);
		new_parent.setDirty(true);
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {		
		new_parent.removeChild(child);
		old_parent.addChildByConstraints(child, old_constraints);
		new_parent.getWidget().validate();
		old_parent.getWidget().validate();
		old_parent.repaintDesigner();
		new_parent.setDirty(true);
		old_parent.setDirty(true);
		return Status.OK_STATUS;
	}
}
