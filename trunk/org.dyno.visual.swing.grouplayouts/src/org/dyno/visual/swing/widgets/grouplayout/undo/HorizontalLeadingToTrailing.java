package org.dyno.visual.swing.widgets.grouplayout.undo;

import java.awt.Component;

import javax.swing.JComponent;

import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class HorizontalLeadingToTrailing extends AbstractOperation {
	private Constraints oldconstraints;
	private JComponent container;
	private Component child;

	public HorizontalLeadingToTrailing(Constraints constraints,
			JComponent container, Component child) {
		super("Set Anchor");
		this.oldconstraints = constraints;
		this.container = container;
		this.child = child;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		Leading leading = (Leading) oldconstraints.getHorizontal();
		int l = leading.getLeading();
		int w = child.getWidth();
		int t = container.getWidth() - l - w;
		Constraints newconstraints = new Constraints(new Trailing(t, 10, l), oldconstraints.getVertical());
		container.remove(child);
		container.add(child, newconstraints);
		container.doLayout();
		container.invalidate();
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(container);
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
		container.remove(child);
		container.add(child, oldconstraints);
		container.doLayout();
		container.invalidate();
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(container);
		adapter.repaintDesigner();
		return Status.OK_STATUS;
	}
}
