package org.dyno.visual.swing.widgets.undo;

import java.awt.Rectangle;
import java.util.List;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public abstract class MoveOperation extends AbstractOperation {
	private WidgetAdapter adapter;

	public MoveOperation(WidgetAdapter adapter) {
		super("");
		setLabel(getName());
		this.adapter = adapter;
	}
	protected abstract String getName();
	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		List<JComponent> selection = adapter.getSelection();
		for (JComponent child : selection) {
			Rectangle bounds = child.getBounds();
			if (isVertical())
				bounds.y += getStepSize();
			else
				bounds.x += getStepSize();
			child.setBounds(bounds);
		}
		adapter.repaintDesigner();
		return Status.OK_STATUS;
	}

	protected abstract int getStepSize();

	protected abstract boolean isVertical();

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		List<JComponent> selection = adapter.getSelection();
		for (JComponent child : selection) {
			Rectangle bounds = child.getBounds();
			if (isVertical())
				bounds.y -= getStepSize();
			else
				bounds.x -= getStepSize();
			child.setBounds(bounds);
		}
		adapter.repaintDesigner();
		return Status.OK_STATUS;
	}
}