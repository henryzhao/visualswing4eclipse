package org.dyno.visual.swing.designer;

import java.awt.Component;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

class DragDropOperation extends AbstractOperation {
	private CompositeAdapter parent;
	private Object constraints;
	private Component child;

	public DragDropOperation(CompositeAdapter parent, Component child, Object constraints) {
		super("Delete Component");
		this.parent = parent;
		this.constraints = constraints;
		this.child = child;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {		
		parent.removeChild(child);
		parent.getWidget().validate();
		parent.repaintDesigner();
		parent.setDirty(true);
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		parent.addChildByConstraints(child, constraints);
		parent.getWidget().validate();
		parent.repaintDesigner();
		parent.setDirty(true);
		return Status.OK_STATUS;
	}
}
