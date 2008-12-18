
/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.widgets.grouplayout.anchor;

import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;

public class HorizontalLeadingAnchor extends HorizontalAnchor {
	public HorizontalLeadingAnchor(JComponent target) {
		super(target);
	}

	@Override
	public Alignment createHoveredAxis(Component me, Rectangle bounds) {
		return createVerticalLeading(me, bounds, target);
	}

	@Override
	public Alignment createBottomAxis(Component me, Rectangle bounds, Alignment lastAxis) {
		return createVerticalLeading(me, bounds, target);
	}

	@Override
	public Alignment createTopAxis(Component me, Rectangle bounds, Alignment lastAxis) {
		if (lastAxis instanceof Leading)
			return createVerticalLeading(me, bounds, target);
		else if (lastAxis instanceof Trailing)
			return createVerticalSpring(me, bounds, target);
		else if (lastAxis instanceof Bilateral) {
			return createVerticalSpring(me, bounds, target);
		}
		return null;
	}
}

