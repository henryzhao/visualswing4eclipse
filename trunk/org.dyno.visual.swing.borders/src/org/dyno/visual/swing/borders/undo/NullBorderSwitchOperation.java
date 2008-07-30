package org.dyno.visual.swing.borders.undo;

import javax.swing.JComponent;
import javax.swing.border.Border;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class NullBorderSwitchOperation extends AbstractOperation {
	private JComponent target;
	private WidgetAdapter targetAdapter;
	private Border prevousBorder;
	public NullBorderSwitchOperation(JComponent w) {
		super("Switch Border");
		this.target = w;
		this.targetAdapter = WidgetAdapter.getWidgetAdapter(target);
		this.prevousBorder = target.getBorder();
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		target.setBorder(null);
		target.repaint();
		targetAdapter.setDirty(true);
		targetAdapter.changeNotify();
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
		targetAdapter.changeNotify();
		return Status.OK_STATUS;
	}
}
