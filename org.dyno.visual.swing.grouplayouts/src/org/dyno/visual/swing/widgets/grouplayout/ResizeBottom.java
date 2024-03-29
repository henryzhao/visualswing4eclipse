
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

package org.dyno.visual.swing.widgets.grouplayout;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JComponent;

import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Spring;
import org.dyno.visual.swing.layouts.Trailing;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

class ResizeBottom extends ResizeOperation {
	public ResizeBottom(GroupLayoutAdapter layout, WidgetAdapter tracingAdapter, GroupLayout op, JComponent container) {
		super(layout, tracingAdapter,op, container);
	}

	
	public boolean dragOver(Point p) {
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		Component todrop = tracingAdapter.getWidget();
		Point lp = p;
		if (last_point == null) {
			last_point = lp;
			adapter.setHovered(true);
			return false;
		}
		if (lp.equals(last_point))
			return false;
		pair = calculateMascotLocation((JComponent)todrop, lp, azimuth);
		Point np = pair == null ? lp : new Point(lp.x, pair.hQuart.masc);
		Point sp = parent.getMascotLocation();
		int cw = todrop.getWidth();
		int ch = np.y - sp.y;
		todrop.setSize(cw, ch);
		azimuth = getAzimuth(p, last_point);
		last_point = lp;
		return true;
	}

	private QuartetPair pair;

	
	public boolean drop(Point p) {
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		Insets insets = container.getInsets();
		WidgetAdapter dropAdapter = tracingAdapter;
		JComponent drop = (JComponent)dropAdapter.getParentContainer();
		Point hot = dropAdapter.getHotspotPoint();
		Alignment vertical = adapter.getLastConstraints().getVertical();
		Alignment horizontal = adapter.getLastConstraints().getHorizontal();
		Point ltp = parent.getMascotLocation();
		int x = ltp.x - hot.x;
		int y = ltp.y - hot.y;
		int width = drop.getWidth();
		int height = drop.getHeight();
		Spring spring = new Spring(10, 10);// TODO should be replaced by a
											// container gap.
		if (pair == null || pair.hQuart == null) {
			if (vertical instanceof Leading)
				vertical = new Leading(y - insets.top, height, spring);
			else if (vertical instanceof Trailing)
				vertical = new Leading(y - insets.top, height, spring);
			else if (vertical instanceof Bilateral)
				vertical = new Leading(y - insets.top, height, spring);
		} else {
			if (pair.hQuart != null) {
				vertical = pair.hQuart.anchor.createBottomAxis(drop, new Rectangle(x, y, width, height), vertical);
			}
		}
		assert vertical != null && horizontal != null;
		Constraints constraints = new Constraints(horizontal, vertical);
		container.add(drop, constraints);
		last_point = null;
		return true;
	}

	
	public boolean dragEnter(Point p) {
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		parent.setMascotLocation(p);
		last_point = p;
		adapter.setHovered(true);
		return true;
	}

	
	public boolean dragExit(Point p) {
		adapter.setHovered(false);
		return true;
	}

	private QuartetPair calculateMascotLocation(JComponent todrop, Point this_point, int azimuth) {
		List<Quartet> hAnchor = calBAnchor(todrop, this_point, azimuth);
		if (hAnchor == null) {
			adapter.addBaseline(null, null);
			return null;
		} else {
			adapter.addBaseline(hAnchor, null);
			Quartet qtet = calMasc(this_point.y, hAnchor);
			return new QuartetPair(qtet, null);
		}
	}
}

