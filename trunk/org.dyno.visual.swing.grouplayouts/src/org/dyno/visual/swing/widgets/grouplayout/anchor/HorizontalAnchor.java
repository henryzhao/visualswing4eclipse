package org.dyno.visual.swing.widgets.grouplayout.anchor;

import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.widgets.grouplayout.Anchor;

public abstract class HorizontalAnchor extends Anchor {
	protected HorizontalAnchor(JComponent target) {
		super(target);
	}

	@Override
	public Alignment createRightAxis(Component me, Rectangle bounds, Alignment lastAxis) {
		return null;
	}

	@Override
	public Alignment createLeftAxis(Component me, Rectangle bounds, Alignment lastAxis) {
		return null;
	}
}
