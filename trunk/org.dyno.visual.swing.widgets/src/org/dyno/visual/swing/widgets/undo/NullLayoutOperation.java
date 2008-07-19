package org.dyno.visual.swing.widgets.undo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.widgets.JPanelAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class NullLayoutOperation extends AbstractOperation {
	private JPanelAdapter adapter;
	private LayoutAdapter oldLayoutAdapter;
	private List<Pair> constraints;

	class Pair {
		JComponent child;
		Object constraints;
	}

	public NullLayoutOperation(JPanelAdapter adapter) {
		super("Set Layout");
		this.adapter = adapter;
		this.oldLayoutAdapter = adapter.getLayoutAdapter();
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		constraints = new ArrayList<Pair>();
		for (int i = 0; i < adapter.getChildCount(); i++) {
			JComponent child = adapter.getChild(i);
			Object cons = adapter.getChildConstraints(child);
			Pair pair = new Pair();
			pair.child = child;
			pair.constraints = cons;
			constraints.add(pair);
		}
		JPanel jpanel = (JPanel) adapter.getWidget();
		jpanel.setLayout(null);
		adapter.setLayoutAdapter(null);
		adapter.doLayout();
		jpanel.validate();
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
		JPanel jpanel = (JPanel) adapter.getWidget();
		jpanel.removeAll();
		if (oldLayoutAdapter != null)
			oldLayoutAdapter.initConainerLayout(jpanel);
		else
			jpanel.setLayout(null);
		for (Pair pair : constraints) {
			adapter.addChildByConstraints(pair.child, pair.constraints);
		}
		adapter.doLayout();
		adapter.setLayoutAdapter(null);
		jpanel.validate();
		adapter.setDirty(true);
		adapter.changeNotify();
		return Status.OK_STATUS;
	}

}
