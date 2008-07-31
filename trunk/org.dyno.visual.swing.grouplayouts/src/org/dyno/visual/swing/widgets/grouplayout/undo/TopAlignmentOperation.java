package org.dyno.visual.swing.widgets.grouplayout.undo;

import java.awt.Component;
import java.util.ArrayList;

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
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class TopAlignmentOperation extends AlignmentOperation {

	public TopAlignmentOperation(JComponent container) {
		super("Align Top", container);
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		GroupLayout layout = (GroupLayout) container.getLayout();
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		WidgetAdapter post = widgets.get(0);
		Component postChild = post.getWidget();
		Constraints postConstraints = layout.getConstraints(postChild);
		Alignment postAlignment = postConstraints.getVertical();
		compcons = new ArrayList<CompCons>();
		if (postAlignment instanceof Leading || postAlignment instanceof Bilateral) {
			int postLead;
			if (postAlignment instanceof Leading)
				postLead = ((Leading) postAlignment).getLeading();
			else
				postLead = ((Bilateral) postAlignment).getLeading();
			for (int i = 1; i < widgets.size(); i++) {
				WidgetAdapter adapter = widgets.get(i);
				Component child = adapter.getWidget();
				Constraints constraints = layout.getConstraints(child);
				CompCons cons = new CompCons();
				cons.component = child;
				cons.constraints = constraints;
				compcons.add(cons);
				Alignment alignment = constraints.getVertical();
				if (alignment instanceof Leading) {
					Leading leading = (Leading) alignment.clone();
					leading.setLeading(postLead);
					constraints = new Constraints(constraints.getHorizontal(), leading);
				} else if (alignment instanceof Bilateral) {
					Bilateral bilateral = (Bilateral) alignment.clone();
					bilateral.setLeading(postLead);
					constraints = new Constraints(constraints.getHorizontal(), bilateral);
				} else if (alignment instanceof Trailing) {
					Bilateral bilateral = new Bilateral(postLead, ((Trailing) alignment).getTrailing(), ((Trailing) alignment).getSize());
					constraints = new Constraints(constraints.getHorizontal(), bilateral);
				}
				layout.setConstraints(child, constraints);
			}
		} else if (postAlignment instanceof Trailing) {
			Trailing postTrailing = (Trailing) postAlignment;
			int postTrail = postTrailing.getTrailing() + postChild.getHeight();
			for (int i = 1; i < widgets.size(); i++) {
				WidgetAdapter adapter = widgets.get(i);
				Component child = adapter.getWidget();
				Constraints constraints = layout.getConstraints(child);
				CompCons cons = new CompCons();
				cons.component = child;
				cons.constraints = constraints;
				compcons.add(cons);
				Alignment alignment = constraints.getVertical();
				if (alignment instanceof Leading) {
					int t = postTrail - child.getHeight();
					Trailing trailing = new Trailing(t, 10, child.getHeight());
					constraints = new Constraints(constraints.getHorizontal(), trailing);
				} else if (alignment instanceof Bilateral) {
					int t = postTrail - child.getHeight();
					Trailing trailing = new Trailing(t, 10, child.getHeight());
					constraints = new Constraints(constraints.getHorizontal(), trailing);
				} else if (alignment instanceof Trailing) {
					Trailing trailing = (Trailing) alignment.clone();
					trailing.setTrailing(postTrail - child.getHeight());
					constraints = new Constraints(constraints.getHorizontal(), trailing);
				}
				layout.setConstraints(child, constraints);
			}
		}
		container.invalidate();
		containerAdapter.doLayout();
		containerAdapter.setDirty(true);
		containerAdapter.repaintDesigner();
		return Status.OK_STATUS;
	}
}
