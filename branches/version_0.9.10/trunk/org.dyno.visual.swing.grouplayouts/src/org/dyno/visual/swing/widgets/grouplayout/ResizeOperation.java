
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IBaselineAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.grouplayout.anchor.HorizontalBaselineAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.HorizontalBottomAlignAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.HorizontalBottomLargeAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.HorizontalBottomRelatedAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.HorizontalBottomUnrelatedAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.HorizontalLeadingContainerAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.HorizontalLeadingGapContainerAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.HorizontalTopAlignAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.HorizontalTopLargeAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.HorizontalTopRelatedAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.HorizontalTopUnrelatedAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.HorizontalTrailingContainerAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.HorizontalTrailingGapContainerAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.VerticalCenterAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.VerticalIndentAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.VerticalLeadingContainerAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.VerticalLeadingGapContainerAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.VerticalLeftAlignAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.VerticalLeftLargeAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.VerticalLeftRelatedAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.VerticalLeftUnrelatedAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.VerticalRightAlignAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.VerticalRightLargeAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.VerticalRightRelatedAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.VerticalRightUnrelatedAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.VerticalTrailingContainerAnchor;
import org.dyno.visual.swing.widgets.grouplayout.anchor.VerticalTrailingGapContainerAnchor;

public abstract class ResizeOperation extends AbstractDragOperation {
	protected ResizeOperation(GroupLayoutAdapter adapter, WidgetAdapter tracingAdapter, GroupLayout layout, JComponent container) {
		super(adapter, tracingAdapter, layout, container);
	}

	protected List<Quartet> calLAnchor(JComponent todrop, Point this_point, int azimuth) {
		azimuth = isIncreasing(SwingConstants.HORIZONTAL, azimuth);
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int size = containerAdapter.getChildCount();
		List<Quartet> vAnchor = null;
		for (int i = 0; i < size; i++) {
			JComponent child = (JComponent)containerAdapter.getChild(i);
			List<Quartet> trios = getLAnchor(todrop, child, this_point);
			if (trios != null) {
				if (vAnchor == null)
					vAnchor = trios;
				else if (compare(trios, vAnchor, azimuth, this_point.x, todrop) < 0)
					vAnchor = trios;
			}
		}
		List<Quartet> trios = getLConAnchor(todrop, this_point);
		if (trios != null) {
			if (vAnchor == null)
				vAnchor = trios;
			else if (compare(trios, vAnchor, azimuth, this_point.x, todrop) < 0)
				vAnchor = trios;
		}
		return vAnchor;
	}

	private int compare(List<Quartet> trios, List<Quartet> anchor, int direction, int pix, JComponent todrop) {
		Quartet min_trios = getMin(trios, pix);
		Quartet min_anchor = getMin(anchor, pix);
		Quartet max_trios = getMax(trios, pix);
		Quartet max_anchor = getMax(anchor, pix);
		switch (direction) {
		case 0:
		case 1:
			if (max_trios.axis < min_anchor.axis)
				return -1;
			if (max_anchor.axis < min_trios.axis)
				return 1;
			JComponent trios_target = max_trios.anchor.target;
			JComponent anchor_target = max_anchor.anchor.target;
			if (SwingUtilities.isDescendingFrom(trios_target, anchor_target))
				return 1;
			if (SwingUtilities.isDescendingFrom(anchor_target, trios_target))
				return -1;
			return 0;
		case -1:
			if (max_trios.axis <= min_anchor.axis)
				return 1;
			if (max_anchor.axis <= min_trios.axis)
				return -1;
			return 0;
		}
		return 0;
	}

	private Quartet getMax(List<Quartet> list, int pix) {
		int max = Integer.MIN_VALUE;
		Quartet result = null;
		for (Quartet quartet : list) {
			if (max < Math.abs(quartet.axis - pix)) {
				max = Math.abs(quartet.axis - pix);
				result = quartet;
			}
		}
		return result;
	}

	private Quartet getMin(List<Quartet> list, int pix) {
		int min = Integer.MAX_VALUE;
		Quartet result = null;
		for (Quartet quartet : list) {
			if (min > Math.abs(quartet.axis - pix)) {
				min = Math.abs(quartet.axis - pix);
				result = quartet;
			}
		}
		return result;
	}

	private List<Quartet> getLConAnchor(JComponent todrop, Point this_point) {
		Insets insets = container.getInsets();
		int tx = this_point.x;
		LayoutStyle style = LayoutStyle.getInstance();
		if (style == null)
			style = LayoutStyle.getInstance();
		int west = style.getContainerGap(todrop, SwingConstants.WEST, container);
		int h = container.getHeight();
		if (Math.abs(tx - insets.left) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(insets.left, insets.left, insets.top, h - insets.bottom, new VerticalLeadingContainerAnchor(container));
			list.add(trio);
			return list;
		} else if (Math.abs(tx - west - insets.left) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(west + insets.left, west + insets.left, insets.top, h - insets.bottom, new VerticalLeadingGapContainerAnchor(container));
			list.add(trio);
			return list;
		} else
			return null;
	}

	private List<Quartet> getLAnchor(JComponent todrop, JComponent target, Point this_point) {
		LayoutStyle style = LayoutStyle.getInstance();
		int wr = style.getPreferredGap(todrop, target, ComponentPlacement.RELATED, SwingConstants.WEST, container);
		int wu = style.getPreferredGap(todrop, target, ComponentPlacement.UNRELATED, SwingConstants.WEST, container);
		int wm = wu + wr;
		int er = style.getPreferredGap(todrop, target, ComponentPlacement.RELATED, SwingConstants.EAST, container);
		int eu = style.getPreferredGap(todrop, target, ComponentPlacement.UNRELATED, SwingConstants.EAST, container);
		int indent = style.getPreferredGap(todrop, target, ComponentPlacement.INDENT, SwingConstants.EAST, container);
		int sr = style.getPreferredGap(todrop, target, ComponentPlacement.RELATED, SwingConstants.SOUTH, container);
		int su = style.getPreferredGap(todrop, target, ComponentPlacement.UNRELATED, SwingConstants.SOUTH, container);
		int tby = target.getY() + target.getHeight();
		sr = sr + tby;
		su = su + tby;
		int em = eu + er;
		int tx = target.getX();
		int trx = target.getX() + target.getWidth();
		wr = tx - wr;
		wu = tx - wu;
		wm = tx - wm;
		er = trx + er;
		eu = trx + eu;
		em = trx + em;
		int tm = target.getWidth() / 2;
		WidgetAdapter parent = WidgetAdapter.getWidgetAdapter(container);
		Point sp = parent.getMascotLocation();
		int sm = ((sp.x + todrop.getWidth()) - this_point.x) / 2;
		int dx = this_point.x;
		tm = tx + tm;
		sm = dx + sm;
		int thisy = sp.y;
		int thisb = thisy + todrop.getHeight();
		int targetx = target.getX();
		int targety = target.getY();
		int targetb = targety + target.getHeight();
		int targeti = targetx + indent;
		int miny = Math.min(thisy, targety) - ANCHOR_EXT;
		int maxb = Math.max(thisb, targetb) + ANCHOR_EXT;
		if (Math.abs(tm - sm) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(tm, 2 * tm - sp.x - todrop.getWidth(), miny, maxb, new VerticalCenterAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(dx - targetx) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(targetx, targetx, miny, maxb, new VerticalLeftAlignAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(dx - targeti) < THRESHOLD_DISTANCE && (Math.abs(thisy - sr) < THRESHOLD_DISTANCE || Math.abs(thisy - su) < THRESHOLD_DISTANCE)) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(targeti, targeti, miny, maxb, new VerticalIndentAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(dx - er) < THRESHOLD_DISTANCE && thisy <= targetb && thisb >= targety) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(er, er, miny, maxb, new VerticalRightRelatedAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(dx - eu) < THRESHOLD_DISTANCE && thisy <= targetb && thisb >= targety) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(er, er, miny, maxb, new VerticalRightRelatedAnchor(target));
			list.add(trio);
			trio = new Quartet(eu, eu, miny, maxb, new VerticalRightUnrelatedAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(dx - em) < THRESHOLD_DISTANCE && thisy <= targetb && thisb >= targety) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(er, er, miny, maxb, new VerticalRightRelatedAnchor(target));
			list.add(trio);
			trio = new Quartet(eu, eu, miny, maxb, new VerticalRightUnrelatedAnchor(target));
			list.add(trio);
			trio = new Quartet(em, em, miny, maxb, new VerticalRightLargeAnchor(target));
			list.add(trio);
			return list;
		}
		return null;
	}

	protected List<Quartet> calBAnchor(JComponent todrop, Point this_point, int azimuth) {
		azimuth = isIncreasing(SwingConstants.VERTICAL, azimuth);
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int size = containerAdapter.getChildCount();
		List<Quartet> hAnchor = null;
		for (int i = 0; i < size; i++) {
			JComponent child = (JComponent)containerAdapter.getChild(i);
			List<Quartet> trios = getBAnchor(todrop, child, this_point);
			if (trios != null) {
				if (hAnchor == null)
					hAnchor = trios;
				else if (compare(trios, hAnchor, azimuth, this_point.y, todrop) < 0)
					hAnchor = trios;
			}
		}
		List<Quartet> trios = getBConAnchor(todrop, this_point);
		if (trios != null) {
			if (hAnchor == null)
				hAnchor = trios;
			else if (compare(trios, hAnchor, azimuth, this_point.y, todrop) < 0)
				hAnchor = trios;
		}
		return hAnchor;
	}

	private List<Quartet> getBConAnchor(JComponent todrop, Point this_point) {
		Insets insets = container.getInsets();
		int by = this_point.y;
		LayoutStyle style = LayoutStyle.getInstance();
		int south = style.getContainerGap(todrop, SwingConstants.SOUTH, container);
		int h = container.getHeight();
		int w = container.getWidth();
		if (Math.abs(by - h + insets.bottom) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(h - insets.bottom, h - insets.bottom, insets.left, w - insets.right, new HorizontalTrailingContainerAnchor(container));
			list.add(trio);
			return list;
		} else if (Math.abs(by - h + insets.bottom + south) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(h - insets.bottom - south, h - insets.bottom - south, insets.left, w - insets.right,
					new HorizontalTrailingGapContainerAnchor(container));
			list.add(trio);
			return list;
		} else
			return null;
	}



	protected List<Quartet> calTAnchor(JComponent todrop, Point this_point, int azimuth) {
		azimuth = isIncreasing(SwingConstants.VERTICAL, azimuth);
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int size = containerAdapter.getChildCount();
		List<Quartet> hAnchor = null;
		for (int i = 0; i < size; i++) {
			JComponent child = (JComponent)containerAdapter.getChild(i);
			List<Quartet> trios = getTAnchor(todrop, child, this_point);
			if (trios != null) {
				if (hAnchor == null)
					hAnchor = trios;
				else if (compare(trios, hAnchor, azimuth, this_point.y, todrop) < 0)
					hAnchor = trios;
			}
		}
		List<Quartet> trios = getTConAnchor(todrop, this_point);
		if (trios != null) {
			if (hAnchor == null)
				hAnchor = trios;
			else if (compare(trios, hAnchor, azimuth, this_point.y, todrop) < 0)
				hAnchor = trios;
		}
		return hAnchor;
	}

	private List<Quartet> getTConAnchor(JComponent todrop, Point this_point) {
		Insets insets = container.getInsets();
		int ty = this_point.y;
		LayoutStyle style = LayoutStyle.getInstance();
		int north = style.getContainerGap(todrop, SwingConstants.NORTH, container);
		int w = container.getWidth();
		if (Math.abs(ty - insets.top) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(insets.top, insets.top, insets.left, w - insets.right, new HorizontalLeadingContainerAnchor(container));
			list.add(trio);
			return list;
		} else if (Math.abs(ty - north - insets.top) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(north + insets.top, north + insets.top, insets.left, w - insets.right,
					new HorizontalLeadingGapContainerAnchor(container));
			list.add(trio);
			return list;
		} else
			return null;
	}
	private List<Quartet> getBAnchor(JComponent todrop, JComponent target, Point this_point) {
		LayoutStyle style = LayoutStyle.getInstance();
		int nr = style.getPreferredGap(todrop, target, ComponentPlacement.RELATED, SwingConstants.NORTH, container);
		int nu = style.getPreferredGap(todrop, target, ComponentPlacement.UNRELATED, SwingConstants.NORTH, container);
		int nm = nr + nu;
		int sr = style.getPreferredGap(todrop, target, ComponentPlacement.RELATED, SwingConstants.SOUTH, container);
		int su = style.getPreferredGap(todrop, target, ComponentPlacement.UNRELATED, SwingConstants.SOUTH, container);
		int sm = sr + su;
		int ty = target.getY();
		int tby = target.getY() + target.getHeight();
		nr = ty - nr;
		nu = ty - nu;
		nm = ty - nm;
		sr = tby + sr;
		su = tby + su;
		sm = tby + sm;
		WidgetAdapter dropAdapter = WidgetAdapter.getWidgetAdapter(todrop);
		IBaselineAdapter dropBaseline=(IBaselineAdapter) dropAdapter.getAdapter(IBaselineAdapter.class);
		WidgetAdapter targetAdapter = WidgetAdapter.getWidgetAdapter(target);
		IBaselineAdapter targetBaseline=(IBaselineAdapter) targetAdapter.getAdapter(IBaselineAdapter.class);
		int tb = targetBaseline.getBaseline();
		WidgetAdapter parent = WidgetAdapter.getWidgetAdapter(container);
		Point sp = parent.getMascotLocation();
		int sb = dropBaseline.getBaseline(this_point.y - sp.y);
		int dby = this_point.y;
		tb = ty + tb;
		sb = sp.y + sb;
		int thisx = sp.x;
		int thisr = thisx + todrop.getWidth();
		int targetx = target.getX();
		int targetr = targetx + target.getWidth();
		int targety = target.getY();
		int targetb = targety + target.getHeight();
		int minx = Math.min(thisx, targetx) - ANCHOR_EXT;
		int maxr = Math.max(thisr, targetr) + ANCHOR_EXT;
		if (Math.abs(dby - targetb) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(targetb, targetb, minx, maxr, new HorizontalBottomAlignAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(tb - sb) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			int dh = dropBaseline.getHeightByBaseline(tb - sp.y);
			Quartet trio = new Quartet(tb, sp.y + dh, minx, maxr, new HorizontalBaselineAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(dby - nr) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(nr, nr, minx, maxr, new HorizontalTopRelatedAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(dby - nu) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(nr, nr, minx, maxr, new HorizontalTopRelatedAnchor(target));
			list.add(trio);
			trio = new Quartet(nu, nu, minx, maxr, new HorizontalTopUnrelatedAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(dby - nm) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(nr, nr, minx, maxr, new HorizontalTopRelatedAnchor(target));
			list.add(trio);
			trio = new Quartet(nu, nu, minx, maxr, new HorizontalTopUnrelatedAnchor(target));
			list.add(trio);
			trio = new Quartet(nm, nm, minx, maxr, new HorizontalTopLargeAnchor(target));
			list.add(trio);
			return list;
		}
		return null;
	}
	private List<Quartet> getTAnchor(JComponent todrop, JComponent target, Point this_point) {
		LayoutStyle style = LayoutStyle.getInstance();
		int nr = style.getPreferredGap(todrop, target, ComponentPlacement.RELATED, SwingConstants.NORTH, container);
		int nu = style.getPreferredGap(todrop, target, ComponentPlacement.UNRELATED, SwingConstants.NORTH, container);
		int nm = nr + nu;
		int sr = style.getPreferredGap(todrop, target, ComponentPlacement.RELATED, SwingConstants.SOUTH, container);
		int su = style.getPreferredGap(todrop, target, ComponentPlacement.UNRELATED, SwingConstants.SOUTH, container);
		int sm = sr + su;
		int ty = target.getY();
		int tby = target.getY() + target.getHeight();
		nr = ty - nr;
		nu = ty - nu;
		nm = ty - nm;
		sr = tby + sr;
		su = tby + su;
		sm = tby + sm;
		WidgetAdapter dropAdapter = WidgetAdapter.getWidgetAdapter(todrop);
		WidgetAdapter targetAdapter = WidgetAdapter.getWidgetAdapter(target);
		WidgetAdapter parent = WidgetAdapter.getWidgetAdapter(container);
		Point sp = parent.getMascotLocation();
		IBaselineAdapter targetBaseline=(IBaselineAdapter) targetAdapter.getAdapter(IBaselineAdapter.class);
		IBaselineAdapter dropBaseline=(IBaselineAdapter) dropAdapter.getAdapter(IBaselineAdapter.class);
		int tb = targetBaseline.getBaseline();
		int sb = dropBaseline.getBaseline(sp.y + todrop.getHeight() - this_point.y);
		int dy = this_point.y;
		tb = ty + tb;
		sb = dy + sb;
		int thisx = todrop.getX();
		int thisr = thisx + todrop.getWidth();
		int targetx = target.getX();
		int targetr = targetx + target.getWidth();
		int targety = target.getY();
		int minx = Math.min(thisx, targetx) - ANCHOR_EXT;
		int maxr = Math.max(thisr, targetr) + ANCHOR_EXT;
		if (Math.abs(tb - sb) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			int by = sp.y + todrop.getHeight();
			int th = by - tb;
			int H = dropBaseline.getHeightByDescent(th);
			Quartet trio = new Quartet(tb, by - H, minx, maxr, new HorizontalBaselineAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(dy - targety) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(targety, targety, minx, maxr, new HorizontalTopAlignAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(dy - sr) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(sr, sr, minx, maxr, new HorizontalBottomRelatedAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(dy - su) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(sr, sr, minx, maxr, new HorizontalBottomRelatedAnchor(target));
			list.add(trio);
			trio = new Quartet(su, su, minx, maxr, new HorizontalBottomUnrelatedAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(dy - sm) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(sr, sr, minx, maxr, new HorizontalBottomRelatedAnchor(target));
			list.add(trio);
			trio = new Quartet(su, su, minx, maxr, new HorizontalBottomUnrelatedAnchor(target));
			list.add(trio);
			trio = new Quartet(sm, sm, minx, maxr, new HorizontalBottomLargeAnchor(target));
			list.add(trio);
			return list;
		}
		return null;
	}

	protected List<Quartet> calRAnchor(JComponent todrop, Point this_point, int azimuth) {
		azimuth = isIncreasing(SwingConstants.HORIZONTAL, azimuth);
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int size = containerAdapter.getChildCount();
		List<Quartet> vAnchor = null;
		for (int i = 0; i < size; i++) {
			JComponent child = (JComponent)containerAdapter.getChild(i);
			List<Quartet> trios = getRAnchor(todrop, child, this_point);
			if (trios != null) {
				if (vAnchor == null)
					vAnchor = trios;
				else if (compare(trios, vAnchor, azimuth, this_point.x, todrop) < 0)
					vAnchor = trios;
			}
		}
		List<Quartet> trios = getRConAnchor(todrop, this_point);
		if (trios != null) {
			if (vAnchor == null)
				vAnchor = trios;
			else if (compare(trios, vAnchor, azimuth, this_point.x, todrop) < 0)
				vAnchor = trios;
		}
		return vAnchor;
	}

	private List<Quartet> getRConAnchor(JComponent todrop, Point this_point) {
		Insets insets = container.getInsets();
		int bx = this_point.x;
		LayoutStyle style = LayoutStyle.getInstance();
		int east = style.getContainerGap(todrop, SwingConstants.EAST, container);
		int h = container.getHeight();
		int w = container.getWidth();
		if (Math.abs(bx - w + insets.right) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(w - insets.right, w - insets.right, insets.top, h - insets.bottom, new VerticalTrailingContainerAnchor(container));
			list.add(trio);
			return list;
		} else if (Math.abs(bx - w + insets.right + east) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(w - insets.right - east, w - insets.right - east, insets.top, h - insets.bottom, new VerticalTrailingGapContainerAnchor(
					container));
			list.add(trio);
			return list;
		} else
			return null;
	}

	private List<Quartet> getRAnchor(JComponent todrop, JComponent target, Point this_point) {
		LayoutStyle style = LayoutStyle.getInstance();
		int wr = style.getPreferredGap(todrop, target, ComponentPlacement.RELATED, SwingConstants.WEST, container);
		int wu = style.getPreferredGap(todrop, target, ComponentPlacement.UNRELATED, SwingConstants.WEST, container);
		int wm = wu + wr;
		int er = style.getPreferredGap(todrop, target, ComponentPlacement.RELATED, SwingConstants.EAST, container);
		int eu = style.getPreferredGap(todrop, target, ComponentPlacement.UNRELATED, SwingConstants.EAST, container);
		int sr = style.getPreferredGap(todrop, target, ComponentPlacement.RELATED, SwingConstants.SOUTH, container);
		int su = style.getPreferredGap(todrop, target, ComponentPlacement.UNRELATED, SwingConstants.SOUTH, container);
		int tby = target.getY() + target.getHeight();
		sr = sr + tby;
		su = su + tby;
		int em = eu + er;
		int tx = target.getX();
		int trx = target.getX() + target.getWidth();
		wr = tx - wr;
		wu = tx - wu;
		wm = tx - wm;
		er = trx + er;
		eu = trx + eu;
		em = trx + em;
		WidgetAdapter dropAdapter = WidgetAdapter.getWidgetAdapter(todrop);
		int tm = target.getWidth() / 2;
		WidgetAdapter parent = WidgetAdapter.getWidgetAdapter(container);
		Point sp = parent.getMascotLocation();
		int sm = (this_point.x - sp.x) / 2;
		int dx = sp.x;
		int drx = this_point.x;
		tm = tx + tm;
		sm = dx + sm;
		int thisy = sp.y;
		int thisb = thisy + todrop.getHeight();
		int targety = target.getY();
		int targetb = targety + target.getHeight();
		int targetx = target.getX();
		int targetr = targetx + target.getWidth();
		int miny = Math.min(thisy, targety) - ANCHOR_EXT;
		int maxb = Math.max(thisb, targetb) + ANCHOR_EXT;
		if (Math.abs(tm - sm) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(tm, 2 * tm - sp.x, miny, maxb, new VerticalCenterAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(drx - targetr) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(targetr, targetr, miny, maxb, new VerticalRightAlignAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(drx - wr) < THRESHOLD_DISTANCE && thisy <= targetb && thisb >= targety) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(wr, wr, miny, maxb, new VerticalLeftRelatedAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(drx - wu) < THRESHOLD_DISTANCE && thisy <= targetb && thisb >= targety) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(wr, wr, miny, maxb, new VerticalLeftRelatedAnchor(target));
			list.add(trio);
			trio = new Quartet(wu, wu, miny, maxb, new VerticalLeftUnrelatedAnchor(target));
			list.add(trio);
			return list;
		} else if (Math.abs(drx - wm) < THRESHOLD_DISTANCE && thisy <= targetb && thisb >= targety) {
			List<Quartet> list = new ArrayList<Quartet>();
			Quartet trio = new Quartet(wr, wr - todrop.getWidth() + dropAdapter.getHotspotPoint().x, miny, maxb, new VerticalLeftRelatedAnchor(target));
			list.add(trio);
			trio = new Quartet(wu, wu, miny, maxb, new VerticalLeftUnrelatedAnchor(target));
			list.add(trio);
			trio = new Quartet(wm, wm, miny, maxb, new VerticalLeftLargeAnchor(target));
			list.add(trio);
			return list;
		}
		return null;
	}

}

