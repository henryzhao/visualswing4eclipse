
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

class ResizeLeft extends ResizeOperation {

	public ResizeLeft(GroupLayoutAdapter layout, WidgetAdapter tracingAdapter, GroupLayout op, JComponent container) {
		super(layout, tracingAdapter,op, container);
	}

	
	public boolean dragOver(Point p) {
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		JComponent todrop = (JComponent)tracingAdapter.getWidget();
		Point lp = p;
		if (last_point == null) {
			last_point = lp;
			adapter.setHovered(true);
			return false;
		}
		if (lp.equals(last_point))
			return false;
		pair = calculateMascotLocation(todrop, lp, azimuth);
		Point np = pair == null ? lp : new Point(pair.vQuart.masc, lp.y);
		Point sp = parent.getMascotLocation();
		int x = sp.x + todrop.getWidth();
		int ch = todrop.getHeight();
		int cw = x - np.x;
		todrop.setSize(cw, ch);
		azimuth = getAzimuth(p, last_point);
		last_point = lp;
		np.y = sp.y;
		parent.setMascotLocation(np);
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
		if (pair == null || pair.vQuart == null) {
			if (horizontal instanceof Leading)
				horizontal = new Leading(x - insets.left, width, spring);
			else if (horizontal instanceof Trailing)
				horizontal = new Trailing(container.getWidth() - x - width - insets.right, width, spring);
			else if (horizontal instanceof Bilateral)
				horizontal = new Trailing(container.getWidth() - x - width - insets.right, width, spring);
		} else {
			if (pair.vQuart != null) {
				horizontal = pair.vQuart.anchor.createLeftAxis(drop, new Rectangle(x, y, width, height), horizontal);
			}
		}
		assert horizontal != null && vertical != null;
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
		List<Quartet> vAnchor = calLAnchor(todrop, this_point, azimuth);
		if (vAnchor == null) {
			adapter.addBaseline(null, null);
			return null;
		} else {
			adapter.addBaseline(null, vAnchor);
			Quartet qtet = calMasc(this_point.x, vAnchor);
			return new QuartetPair(null, qtet);
		}
	}
}

