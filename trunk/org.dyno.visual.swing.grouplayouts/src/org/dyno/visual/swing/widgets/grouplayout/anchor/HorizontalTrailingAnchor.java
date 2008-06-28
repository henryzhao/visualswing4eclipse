package org.dyno.visual.swing.widgets.grouplayout.anchor;

import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;

public class HorizontalTrailingAnchor extends HorizontalAnchor {
	public HorizontalTrailingAnchor(JComponent target) {
		super(target);
	}

	@Override
	public Alignment createHoveredAxis(Component me, Rectangle bounds) {
		return createVerticalTrailing(me, bounds, target);
	}

	@Override
	public Alignment createBottomAxis(Component me, Rectangle bounds, Alignment lastAxis) {
		if (lastAxis instanceof Leading) {
			return createVerticalSpring(me, bounds, target);
		} else if (lastAxis instanceof Trailing) {
			return createVerticalTrailing(me, bounds, target);
		} else if (lastAxis instanceof Bilateral) {
			return createVerticalSpring(me, bounds, target);
		}
		return null;
	}

	@Override
	public Alignment createTopAxis(Component me, Rectangle bounds, Alignment lastAxis) {
		return createVerticalTrailing(me, bounds, target);
	}
}
