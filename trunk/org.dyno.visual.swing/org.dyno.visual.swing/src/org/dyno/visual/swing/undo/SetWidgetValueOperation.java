package org.dyno.visual.swing.undo;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class SetWidgetValueOperation extends AbstractOperation {
	private WidgetAdapter adapter;
	private Object old_value;
	private Object new_value;
	public SetWidgetValueOperation(WidgetAdapter adapter, Object new_value){
		super("changing component value");
		this.adapter = adapter;
		this.old_value = adapter.getWidgetValue();
		this.new_value = new_value;
	}
	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		adapter.setWidgetValue(new_value);
		adapter.repaintDesigner();
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {		
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		adapter.setWidgetValue(old_value);
		adapter.repaintDesigner();
		return Status.OK_STATUS;
	}

}
