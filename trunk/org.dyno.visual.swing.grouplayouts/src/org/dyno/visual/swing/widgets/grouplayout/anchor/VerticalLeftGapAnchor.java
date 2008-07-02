package org.dyno.visual.swing.widgets.grouplayout.anchor;

import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;

public class VerticalLeftGapAnchor extends VerticalAnchor {
	public VerticalLeftGapAnchor(JComponent target) {
		super(target);
	}

	@Override
	public Alignment createHoveredAxis(Component me, Rectangle bounds) {
		Container parent = target.getParent();
		GroupLayout layout = (GroupLayout) parent.getLayout();
		Constraints constraints = layout.getConstraints(target);
		Alignment horizontal = constraints.getHorizontal();
		if (horizontal instanceof Leading) {
			return createHorizontalLeading(me, bounds, parent);
		} else if (horizontal instanceof Trailing) {
			return createHorizontalTrailing(me, bounds, parent);
		} else if (horizontal instanceof Bilateral) {
			return createHorizontalLeading(me, bounds, parent);
		}
		return null;
	}

	@Override
	public Alignment createRightAxis(Component me, Rectangle bounds, Alignment lastAxis) {
		Container parent = target.getParent();
		GroupLayout layout = (GroupLayout) parent.getLayout();
		Constraints constraints = layout.getConstraints(target);
		Alignment horizontal = constraints.getHorizontal();
		if (lastAxis instanceof Leading) {
			if (horizontal instanceof Leading) {
				return createHorizontalLeading(me, bounds, parent);
			} else if (horizontal instanceof Trailing) {
				return createHorizontalSpring(me, bounds, parent);
			} else if (horizontal instanceof Bilateral) {
				return createHorizontalLeading(me, bounds, parent);
			}
		} else if (lastAxis instanceof Trailing) {
			if (horizontal instanceof Leading) {
				return createHorizontalLeading(me, bounds, parent);
			} else if (horizontal instanceof Trailing) {
				return createHorizontalTrailing(me, bounds, parent);
			} else if (horizontal instanceof Bilateral) {
				return createHorizontalLeading(me, bounds, parent);
			}
		} else if (lastAxis instanceof Bilateral) {
			if (horizontal instanceof Leading) {
				return createHorizontalLeading(me, bounds, parent);
			} else if (horizontal instanceof Trailing) {
				return createHorizontalSpring(me, bounds, parent);
			} else if (horizontal instanceof Bilateral) {
				return createHorizontalLeading(me, bounds, parent);
			}
		}
		return null;
	}

	@Override
	public Alignment createLeftAxis(Component me, Rectangle bounds, Alignment lastAxis) {
		Container parent = target.getParent();
		GroupLayout layout = (GroupLayout) parent.getLayout();
		Constraints constraints = layout.getConstraints(target);
		Alignment horizontal = constraints.getHorizontal();
		if (horizontal instanceof Leading) {
			return createHorizontalLeading(me, bounds, parent);
		} else if (horizontal instanceof Trailing) {
			return createHorizontalTrailing(me, bounds, parent);
		} else if (horizontal instanceof Bilateral) {
			return createHorizontalLeading(me, bounds, parent);
		}
		return null;
	}
}
