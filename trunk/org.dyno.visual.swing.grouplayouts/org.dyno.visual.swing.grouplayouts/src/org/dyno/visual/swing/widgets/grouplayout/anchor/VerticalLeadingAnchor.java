/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.grouplayout.anchor;

import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;

public class VerticalLeadingAnchor extends VerticalAnchor {
	public VerticalLeadingAnchor(JComponent target) {
		super(target);
	}

	@Override
	public Alignment createHoveredAxis(Component me, Rectangle bounds) {
		return createHorizontalLeading(me, bounds, target);
	}

	@Override
	public Alignment createRightAxis(Component me, Rectangle bounds, Alignment lastAxis) {
		if (lastAxis instanceof Leading) {
			return createHorizontalLeading(me, bounds, target);
		} else if (lastAxis instanceof Trailing) {
			return createHorizontalTrailing(me, bounds, target);
		} else if (lastAxis instanceof Bilateral) {
			return createHorizontalSpring(me, bounds, target);
		}
		return null;
	}

	@Override
	public Alignment createLeftAxis(Component me, Rectangle bounds, Alignment lastAxis) {
		if (lastAxis instanceof Leading) {
			return createHorizontalLeading(me, bounds, target);
		} else if (lastAxis instanceof Trailing) {
			return createHorizontalSpring(me, bounds, target);
		} else if (lastAxis instanceof Bilateral) {
			return createHorizontalSpring(me, bounds, target);
		}
		return null;
	}
}
