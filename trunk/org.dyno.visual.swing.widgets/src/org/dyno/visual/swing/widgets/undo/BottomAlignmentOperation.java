package org.dyno.visual.swing.widgets.undo;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class BottomAlignmentOperation extends AbstractOperation {
	private List<WidgetAdapter>selection;
	private List<Integer> boundy;
	public BottomAlignmentOperation(List<WidgetAdapter>selected) {
		super("Align Left");
		selection = new ArrayList<WidgetAdapter>();
		for(WidgetAdapter adapter:selected){
			selection.add(adapter);
		}
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		int y = -1;
		boundy = new ArrayList<Integer>();
		for (int i=0;i<selection.size();i++) {
			WidgetAdapter childAdapter = selection.get(i);				
			JComponent child = childAdapter.getWidget();
			if (y == -1){
				boundy.add(child.getY());
				y = child.getY() + child.getHeight();
			} else {
				Rectangle bounds = child.getBounds();
				boundy.add(bounds.y);
				bounds.y = y - bounds.height;
				child.setBounds(bounds);
			}
		}
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		for (int i=0;i<selection.size();i++) {
			WidgetAdapter childAdapter = selection.get(i);				
			JComponent child = childAdapter.getWidget();
			Rectangle bounds = child.getBounds();
			bounds.y = boundy.get(i);
			child.setBounds(bounds);
		}
		return Status.OK_STATUS;
	}
}
