package org.dyno.visual.swing.undo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.dyno.visual.swing.designer.WidgetSelection;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class CutOperation extends AbstractOperation {
	private WidgetSelection selection;
	private List<CompositeAdapter>parents;
	public CutOperation(WidgetSelection selection) {
		super("Cut Components");
		this.selection = selection;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		parents = new ArrayList<CompositeAdapter>();
		for (JComponent child : selection) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			CompositeAdapter parentAdapter=adapter.getParentAdapter();
			boolean success = parentAdapter.removeChild(child);
			if (success)
				parentAdapter.setDirty(true);
			parents.add(parentAdapter);
		}
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		for(int i=0;i<selection.size();i++){
			JComponent child = selection.get(i);
			CompositeAdapter parentAdapter = parents.get(i);
			parentAdapter.addChild(child);
			parentAdapter.setDirty(true);
			parentAdapter.repaintDesigner();
		}
		return Status.OK_STATUS;
	}

}
