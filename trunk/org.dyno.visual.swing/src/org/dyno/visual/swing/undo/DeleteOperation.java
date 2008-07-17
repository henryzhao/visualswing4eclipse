package org.dyno.visual.swing.undo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class DeleteOperation extends AbstractOperation {
	class ParentConstraints {
		JComponent child;
		JComponent parent;
		Object constraints;
	}

	private List<JComponent> selection;
	private JComponent root;
	private List<ParentConstraints> constraints;

	public DeleteOperation(List<JComponent> selection, JComponent root) {
		super("Delete Component");
		this.selection = selection;
		this.root = root;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		constraints = new ArrayList<ParentConstraints>();
		CompositeAdapter rootAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter((JComponent) root);
		for (JComponent child : selection) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			CompositeAdapter parentAdapter = (CompositeAdapter) adapter.getParentAdapter();
			ParentConstraints pc = new ParentConstraints();
			pc.parent = parentAdapter.getWidget();
			pc.constraints = parentAdapter.getChildConstraints(child);
			pc.child = child;
			constraints.add(pc);
			boolean success = parentAdapter.removeChild(child);
			parentAdapter.doLayout();
			parentAdapter.getWidget().validate();
			if (success)
				parentAdapter.setDirty(true);
		}
		rootAdapter.doLayout();
		root.validate();
		rootAdapter.repaintDesigner();
		rootAdapter.getDesigner().publishSelection();
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		CompositeAdapter rootAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter((JComponent) root);
		for (ParentConstraints pc : constraints) {
			CompositeAdapter parentAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(pc.parent);
			parentAdapter.addChildByConstraints(pc.child, pc.constraints);
			parentAdapter.doLayout();
			parentAdapter.getWidget().validate();
			parentAdapter.setDirty(true);
		}
		rootAdapter.doLayout();
		root.validate();
		rootAdapter.repaintDesigner();
		rootAdapter.getDesigner().publishSelection();
		return Status.OK_STATUS;
	}

}
