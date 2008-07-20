package org.dyno.visual.swing.widgets.grouplayout.undo;

import java.util.List;

import javax.swing.JComponent;

import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class TopAlignmentOperation extends AbstractOperation {
	private JComponent container;
	public TopAlignmentOperation(JComponent container) {
		super("Align Top");
		this.container = container;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		GroupLayout layout = (GroupLayout) container.getLayout();
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		List<WidgetAdapter> widgets = containerAdapter.getSelectedWidgets();
		WidgetAdapter post = widgets.get(0);
		JComponent postChild = post.getWidget();
		Constraints postConstraints = layout.getConstraints(postChild);
		Alignment postAlignment = postConstraints.getVertical();
		if (postAlignment instanceof Leading || postAlignment instanceof Bilateral) {
			int postLead;
			if(postAlignment instanceof Leading)
				postLead = ((Leading) postAlignment).getLeading();
			else 
				postLead = ((Bilateral)postAlignment).getLeading(); 
			for (int i = 1; i < widgets.size(); i++) {
				WidgetAdapter adapter = widgets.get(i);
				JComponent child = adapter.getWidget();
				Constraints constraints = layout.getConstraints(child);
				Alignment alignment = constraints.getVertical();
				if (alignment instanceof Leading) {
					((Leading) alignment).setLeading(postLead);
				} else if (alignment instanceof Bilateral) {
					((Bilateral) alignment).setLeading(postLead);
				} else if (alignment instanceof Trailing) {
					Bilateral bilateral = new Bilateral(postLead, ((Trailing) alignment).getTrailing(), ((Trailing) alignment).getSize());
					constraints.setVertical(bilateral);
				}
			}
		} else if (postAlignment instanceof Trailing) {
			Trailing postTrailing = (Trailing) postAlignment;
			int postTrail = postTrailing.getTrailing()+postChild.getHeight();
			for (int i = 1; i < widgets.size(); i++) {
				WidgetAdapter adapter = widgets.get(i);
				JComponent child = adapter.getWidget();
				Constraints constraints = layout.getConstraints(child);
				Alignment alignment = constraints.getVertical();
				if (alignment instanceof Leading) {
					int t = postTrail-child.getHeight();
					Trailing trailing = new Trailing(t, 10, child.getHeight());
					constraints.setVertical(trailing);
				} else if (alignment instanceof Bilateral) {
					int t = postTrail-child.getHeight();
					Trailing trailing = new Trailing(t, 10, child.getHeight());
					constraints.setVertical(trailing);
				} else if (alignment instanceof Trailing) {
					((Trailing)alignment).setTrailing(postTrail-child.getHeight());
				}
			}
		}
		container.invalidate();
		containerAdapter.doLayout();
		containerAdapter.setDirty(true);
		containerAdapter.repaintDesigner();
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

}
