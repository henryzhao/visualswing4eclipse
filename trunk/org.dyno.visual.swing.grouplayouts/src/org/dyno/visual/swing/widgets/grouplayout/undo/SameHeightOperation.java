package org.dyno.visual.swing.widgets.grouplayout.undo;

import java.awt.Component;
import java.awt.Insets;
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

public class SameHeightOperation extends AlignmentOperation {

	public SameHeightOperation(JComponent container) {
		super("Same Height", container);
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		GroupLayout layout = (GroupLayout) container.getLayout();
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		WidgetAdapter post = widgets.get(0);
		Component postChild = post.getWidget();
		int postSize = postChild.getHeight();
		Constraints postConstraints = layout.getConstraints(postChild);
		Alignment postAlignment = postConstraints.getVertical();
		compcons = new ArrayList<CompCons>();
		Insets insets = container.getInsets();
		int innerHeight = container.getHeight() - insets.top - insets.bottom;
		if (postAlignment instanceof Leading) {
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
					leading.setSize(postSize);
					constraints = new Constraints(constraints.getHorizontal(), leading);
				} else if (alignment instanceof Bilateral) {
					Bilateral bilateral = (Bilateral) alignment;
					int lead = bilateral.getLeading();
					Leading leading = new Leading(lead, postSize, 10, innerHeight - postSize - lead);
					constraints = new Constraints(constraints.getHorizontal(), leading);
				} else if (alignment instanceof Trailing) {
					Trailing trailing = (Trailing) alignment.clone();
					trailing.setSize(postSize);
					constraints = new Constraints(constraints.getHorizontal(), trailing);
				}
				layout.setConstraints(child, constraints);
			}
		} else if (postAlignment instanceof Trailing) {
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
					leading.setSize(postSize);
					constraints = new Constraints(constraints.getHorizontal(), leading);
				} else if (alignment instanceof Bilateral) {
					Bilateral bilateral = (Bilateral) alignment;
					int trail = bilateral.getTrailing();
					Trailing leading = new Trailing(trail, postSize, 10, innerHeight - postSize - trail);
					constraints = new Constraints(constraints.getHorizontal(), leading);
				} else if (alignment instanceof Trailing) {
					Trailing trailing = (Trailing) alignment.clone();
					trailing.setSize(postSize);
					constraints = new Constraints(constraints.getHorizontal(), trailing);
				}
				layout.setConstraints(child, constraints);
			}
		} else if (postAlignment instanceof Bilateral) {
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
					Leading leading = (Leading) alignment;
					Bilateral bilateral = (Bilateral) postAlignment.clone();
					bilateral.setLeading(leading.getLeading());
					bilateral.setTrailing(innerHeight-leading.getLeading()-child.getHeight());
					constraints = new Constraints(constraints.getHorizontal(), bilateral);
				} else if (alignment instanceof Bilateral) {
					Bilateral leading = (Bilateral) alignment;
					Bilateral bilateral = (Bilateral) postAlignment.clone();
					bilateral.setTrailing(leading.getTrailing());
					bilateral.setLeading(leading.getLeading());
					constraints = new Constraints(constraints.getHorizontal(), bilateral);
				} else if (alignment instanceof Trailing) {
					Trailing trailing = (Trailing) alignment;
					Bilateral bilateral = (Bilateral) postAlignment.clone();
					bilateral.setTrailing(trailing.getTrailing());
					bilateral.setLeading(innerHeight-trailing.getTrailing()-child.getHeight());
					constraints = new Constraints(constraints.getHorizontal(), bilateral);
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
