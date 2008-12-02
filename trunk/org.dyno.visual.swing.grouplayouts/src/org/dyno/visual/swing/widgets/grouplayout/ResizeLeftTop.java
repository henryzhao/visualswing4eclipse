/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.grouplayout;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JComponent;

import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Spring;
import org.dyno.visual.swing.layouts.Trailing;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

class ResizeLeftTop extends ResizeOperation {
	public ResizeLeftTop(GroupLayoutAdapter layout, WidgetAdapter tracingAdapter, GroupLayout op, JComponent container) {
		super(layout,tracingAdapter, op, container);
	}

	@Override
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
		Point np = pair == null ? lp : new Point(pair.vQuart == null ? lp.x : pair.vQuart.masc, pair.hQuart == null ? lp.y : pair.hQuart.masc);
		Point sp = parent.getMascotLocation();
		parent.setMascotLocation(np);
		int x = sp.x + todrop.getWidth();
		int y = sp.y + todrop.getHeight();
		int cw = x - np.x;
		int ch = y - np.y;
		todrop.setSize(cw, ch);
		azimuth = getAzimuth(p, last_point);
		last_point = lp;
		return true;
	}

	private QuartetPair pair;

	@Override
	public boolean drop(Point p) {
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		Insets insets = container.getInsets();
		WidgetAdapter dropAdapter = tracingAdapter;
		JComponent drop = (JComponent)dropAdapter.getComponent();
		Point hot = dropAdapter.getHotspotPoint();
		Constraints cons = adapter.getLastConstraints();
		Alignment horizontal = cons.getHorizontal();
		Alignment vertical = cons.getVertical();
		Point ltp = parent.getMascotLocation();
		int x = ltp.x - hot.x;
		int y = ltp.y - hot.y;
		int width = drop.getWidth();
		int height = drop.getHeight();
		Spring spring = new Spring(10, 10);// TODO should be replaced by a
		// container gap.
		if (pair == null) {
			if (horizontal instanceof Leading)
				horizontal = new Leading(x - insets.left, width, spring);
			else if (horizontal instanceof Trailing)
				horizontal = new Trailing(container.getWidth() - x - width - insets.right, width, spring);
			else if (horizontal instanceof Bilateral)
				horizontal = new Trailing(container.getWidth() - x - width - insets.right, width, spring);
			if (vertical instanceof Leading)
				vertical = new Leading(y - insets.top, height, spring);
			else if (vertical instanceof Trailing)
				vertical = new Trailing(container.getHeight() - y - height - insets.bottom, height, spring);
			else if (vertical instanceof Bilateral)
				vertical = new Trailing(container.getHeight() - y - height - insets.bottom, height, spring);
		} else {
			if (pair.vQuart != null) {
				horizontal = pair.vQuart.anchor.createLeftAxis(drop, new Rectangle(x, y, width, height), horizontal);
			} else {
				if (horizontal instanceof Leading)
					horizontal = new Leading(x - insets.left, width, spring);
				else if (horizontal instanceof Trailing)
					horizontal = new Trailing(container.getWidth() - x - width - insets.right, width, spring);
				else if (horizontal instanceof Bilateral)
					horizontal = new Trailing(container.getWidth() - x - width - insets.right, width, spring);
			}
			if (pair.hQuart != null) {
				vertical = pair.hQuart.anchor.createTopAxis(drop, new Rectangle(x, y, width, height), vertical);
			} else {
				if (vertical instanceof Leading)
					vertical = new Leading(y - insets.top, height, spring);
				else if (vertical instanceof Trailing)
					vertical = new Trailing(container.getHeight() - y - height - insets.bottom, height, spring);
				else if (vertical instanceof Bilateral)
					vertical = new Trailing(container.getHeight() - y - height - insets.bottom, height, spring);
			}
		}
		assert horizontal != null && vertical != null;
		Constraints constraints = new Constraints(horizontal, vertical);
		container.add(drop, constraints);
		last_point = null;
		return true;
	}

	@Override
	public boolean dragEnter(Point p) {
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		parent.setMascotLocation(p);
		last_point = p;
		adapter.setHovered(true);
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		adapter.setHovered(false);
		return true;
	}

	private QuartetPair calculateMascotLocation(JComponent todrop, Point this_point, int azimuth) {
		List<Quartet> hAnchor = calTAnchor(todrop, this_point, azimuth);
		List<Quartet> vAnchor = calLAnchor(todrop, this_point, azimuth);
		if (hAnchor == null) {
			if (vAnchor == null) {
				adapter.addBaseline(null, null);
				return null;
			} else {
				adapter.addBaseline(null, vAnchor);
				Quartet qtet = calMasc(this_point.x, vAnchor);
				return new QuartetPair(null, qtet);
			}
		} else {
			if (vAnchor == null) {
				adapter.addBaseline(hAnchor, null);
				Quartet qtet = calMasc(this_point.y, hAnchor);
				return new QuartetPair(qtet, null);
			} else {
				adapter.addBaseline(hAnchor, vAnchor);
				Quartet vqtet = calMasc(this_point.x, vAnchor);
				Quartet hqtet = calMasc(this_point.y, hAnchor);
				return new QuartetPair(hqtet, vqtet);
			}
		}
	}
}
