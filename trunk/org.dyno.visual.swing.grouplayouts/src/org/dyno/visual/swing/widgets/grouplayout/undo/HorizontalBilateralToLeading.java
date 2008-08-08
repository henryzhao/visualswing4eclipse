package org.dyno.visual.swing.widgets.grouplayout.undo;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.JComponent;

import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class HorizontalBilateralToLeading extends AbstractOperation {
	private Constraints oldconstraints;
	private JComponent container;
	private Component child;

	public HorizontalBilateralToLeading(Constraints constraints,
			JComponent container, Component child) {
		super("Set Anchor");
		this.oldconstraints = constraints;
		this.container = container;
		this.child = child;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		Bilateral bilateral = (Bilateral) oldconstraints.getHorizontal();
		int l = bilateral.getLeading();
		int w = child.getWidth();
		Insets insets = container.getInsets();
		int t = container.getWidth() - insets.left - insets.right - l - w;
		Constraints newconstraints = new Constraints(new Leading(l, 10, t),
				oldconstraints.getVertical());
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
