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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
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

abstract class AbstractDragOperation implements IDragOperation {
	protected GroupLayoutAdapter adapter;
	protected JComponent container;
	protected GroupLayout layout;
	protected Point last_point;
	protected int azimuth;

	protected AbstractDragOperation(GroupLayoutAdapter adapter, GroupLayout layout, JComponent container) {
		this.adapter = adapter;
		this.container = container;
		this.layout = layout;
	}

	private int compare(List<Quartet> trios, List<Quartet> anchor, int direction) {
		int min_trios = getMin(trios);
		int min_anchor = getMin(anchor);
		int max_trios = getMax(trios);
		int max_anchor = getMax(anchor);
		switch (direction) {
		case 0:
			if (max_trios <= min_anchor)
				return -1;
			if (max_anchor <= min_trios)
				return 1;
			return 0;
		case 1:
			return -1;
		case -1:
			return 1;
		}
		return 0;
	}

	private int getMax(List<Quartet> list) {
		int max = Integer.MIN_VALUE;
		for (Quartet quartet : list) {
			if (max < quartet.axis)
				max = quartet.axis;
		}
		return max;
	}

	private int getMin(List<Quartet> list) {
		int min = Integer.MAX_VALUE;
		for (Quartet quartet : list) {
			if (min > quartet.axis)
				min = quartet.axis;
		}
		return min;
	}

	protected int isIncreasing(int dimension, int azimuth) {
		if (dimension == SwingConstants.HORIZONTAL) {
			switch (azimuth) {
			case NORTH_EAST:
				return 1;
			case EAST:
				return 1;
			case SOUTH_EAST:
				return 1;
			case NORTH:
				return 0;
			case SOUTH:
				return 0;
			case NORTH_WEST:
				return -1;
			case SOUTH_WEST:
				return -1;
			case WEST:
				return -1;
			}
		} else {
			switch (azimuth) {
			case NORTH_EAST:
				return -1;
			case EAST:
				return 0;
			case SOUTH_EAST:
				return 1;
			case NORTH:
				return -1;
			case SOUTH:
				return 1;
			case NORTH_WEST:
				return -1;
			case SOUTH_WEST:
				return 1;
			case WEST:
				return 0;
			}
		}
		return 0;
	}

	protected Point dragComponent(Point p) {
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		Point lp = p;
		QuartetPair pair = calMascotLocation(parent.getDropWidget().getWidget(), lp, azimuth);
		Point np = pair == null ? lp : new Point(pair.vQuart == null ? lp.x : pair.vQuart.masc, pair.hQuart == null ? lp.y : pair.hQuart.masc);
		azimuth = getAzimuth(p, last_point);
		last_point = lp;
		Point newp = np;
		Point oldp = parent.getMascotLocation();
		parent.setMascotLocation(newp);
		return oldp;
	}
	protected QuartetPair calMascotLocation(JComponent todrop, Point this_point, int azimuth) {
		List<Quartet> hAnchor = calHAnchor(todrop, this_point, azimuth);
		List<Quartet> vAnchor = calVAnchor(todrop, this_point, azimuth);
		if (hAnchor == null) {
			if (vAnchor == null) {
				adapter.setBaseline(null, null);
				return null;
			} else {
				adapter.setBaseline(null, vAnchor);
				Quartet qtet = calMasc(this_point.x, vAnchor);
				return new QuartetPair(null, qtet);
			}
		} else {
			if (vAnchor == null) {
				adapter.setBaseline(hAnchor, null);
				Quartet qtet = calMasc(this_point.y, hAnchor);
				return new QuartetPair(qtet, null);
			} else {
				adapter.setBaseline(hAnchor, vAnchor);
				Quartet vqtet = calMasc(this_point.x, vAnchor);
				Quartet hqtet = calMasc(this_point.y, hAnchor);
				return new QuartetPair(hqtet, vqtet);
			}
		}
	}

	private List<Quartet> calVAnchor(JComponent todrop, Point this_point, int azimuth) {
		azimuth = isIncreasing(SwingConstants.HORIZONTAL, azimuth);
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int size = containerAdapter.getChildCount();
		List<Quartet> vAnchor = null;
		for (int i = 0; i < size; i++) {
			JComponent child = containerAdapter.getChild(i);
			List<Quartet> trios = getVAnchor(todrop, child, this_point);
			if (trios != null) {
				if (vAnchor == null)
					vAnchor = trios;
				else if (compare(trios, vAnchor, azimuth) <= 0)
					vAnchor = trios;
			}
		}
		List<Quartet> trios = getVConAnchor(todrop, this_point);
		if (trios != null) {
			if (vAnchor == null)
				vAnchor = trios;
			else if (compare(trios, vAnchor, azimuth) <= 0)
				vAnchor = trios;
		}
		return vAnchor;
	}

	private List<Quartet> calHAnchor(JComponent todrop, Point this_point, int azimuth) {
		azimuth = isIncreasing(SwingConstants.VERTICAL, azimuth);
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int size = containerAdapter.getChildCount();
		List<Quartet> hAnchor = null;
		for (int i = 0; i < size; i++) {
			JComponent child = containerAdapter.getChild(i);
			List<Quartet> trios = getHAnchor(todrop, child, this_point);
			if (trios != null) {
				if (hAnchor == null)
					hAnchor = trios;
				else if (compare(trios, hAnchor, azimuth) <= 0)
					hAnchor = trios;
			}
		}
		List<Quartet> trios = getHConAnchor(todrop, this_point);
		if (trios != null) {
			if (hAnchor == null)
				hAnchor = trios;
			else if (compare(trios, hAnchor, azimuth) <= 0)
				hAnchor = trios;
		}
		return hAnchor;
	}

	private List<Quartet> getHConAnchor(JComponent todrop, Point this_point) {
		Insets insets = container.getInsets();
		WidgetAdapter dropAdapter = WidgetAdapter.getWidgetAdapter(todrop);
		int hotspoty = dropAdapter.getHotspotPoint().y;
		int ty = this_point.y - hotspoty;
		int by = ty + todrop.getHeight();
		LayoutStyle style = LayoutStyle.getInstance();
		int north = style.getContainerGap(todrop, SwingConstants.NORTH, container);
		int south = style.getContainerGap(todrop, SwingConstants.SOUTH, container);
		int h = container.getHeight();
		int w = container.getWidth();
		if (Math.abs(ty - insets.top) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new HorizontalLeadingContainerAnchor(container);
			Quartet trio = new Quartet(insets.top, insets.top + hotspoty, insets.left, w - insets.right, a);
			list.add(trio);
			return list;
		}
		if (Math.abs(ty - north - insets.top) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new HorizontalLeadingGapContainerAnchor(container);
			Quartet trio = new Quartet(insets.top + north, insets.top + hotspoty + north, insets.left, w - insets.right, a);
			list.add(trio);
			return list;
		}
		if (Math.abs(by - h + insets.bottom) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new HorizontalTrailingContainerAnchor(container);
			Quartet trio = new Quartet(h - insets.bottom, h - insets.bottom - todrop.getHeight() + hotspoty, insets.left, w - insets.right, a);
			list.add(trio);
			return list;
		}
		if (Math.abs(by - h + south + insets.bottom) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new HorizontalTrailingGapContainerAnchor(container);
			Quartet trio = new Quartet(h - insets.bottom - south, h - insets.bottom - south - todrop.getHeight() + hotspoty, insets.left, w - insets.right, a);
			list.add(trio);
			return list;
		}
		return null;
	}

	private List<Quartet> getVConAnchor(JComponent todrop, Point this_point) {
		Insets insets = container.getInsets();
		WidgetAdapter dropAdapter = WidgetAdapter.getWidgetAdapter(todrop);
		int hotspotx = dropAdapter.getHotspotPoint().x;
		int tx = this_point.x - hotspotx;
		int bx = tx + todrop.getWidth();
		LayoutStyle style = LayoutStyle.getInstance();
		int west = style.getContainerGap(todrop, SwingConstants.WEST, container);
		int east = style.getContainerGap(todrop, SwingConstants.EAST, container);
		int h = container.getHeight();
		int w = container.getWidth();
		if (Math.abs(tx - insets.left) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new VerticalLeadingContainerAnchor(container);
			Quartet trio = new Quartet(insets.left, insets.left + hotspotx, insets.top, h - insets.bottom, a);
			list.add(trio);
			return list;
		} else if (Math.abs(tx - west - insets.left) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new VerticalLeadingGapContainerAnchor(container);
			Quartet trio = new Quartet(west + insets.left, insets.left + hotspotx + west, insets.top, h - insets.bottom, a);
			list.add(trio);
			return list;
		} else if (Math.abs(bx - w + insets.right) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new VerticalTrailingContainerAnchor(container);
			Quartet trio = new Quartet(w - insets.right, w - insets.right - todrop.getWidth() + hotspotx, insets.top, h - insets.bottom, a);
			list.add(trio);
			return list;
		} else if (Math.abs(bx - w + insets.right + east) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new VerticalTrailingGapContainerAnchor(container);
			Quartet trio = new Quartet(w - insets.right - east, w - insets.right - east - todrop.getWidth() + hotspotx, insets.top, h - insets.bottom, a);
			list.add(trio);
			return list;
		} else
			return null;
	}

	private List<Quartet> getVAnchor(JComponent todrop, JComponent target, Point this_point) {
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
		WidgetAdapter dropAdapter = WidgetAdapter.getWidgetAdapter(todrop);
		int tm = target.getWidth() / 2;
		int sm = todrop.getWidth() / 2;
		int dx = this_point.x - dropAdapter.getHotspotPoint().x;
		int drx = dx + todrop.getWidth();
		tm = tx + tm;
		sm = dx + sm;
		int thisy = this_point.y - dropAdapter.getHotspotPoint().y;
		int thisb = thisy + todrop.getHeight();
		int targetx = target.getX();
		int targetr = targetx + target.getWidth();
		int targety = target.getY();
		int targetb = targety + target.getHeight();
		int targeti = targetx + indent;
		int miny = Math.min(thisy, targety) - ANCHOR_EXT;
		int maxb = Math.max(thisb, targetb) + ANCHOR_EXT;
		if (Math.abs(tm - sm) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new VerticalCenterAnchor(target);
			Quartet trio = new Quartet(tm, tm - todrop.getWidth() / 2 + dropAdapter.getHotspotPoint().x, miny, maxb, a);
			list.add(trio);
			return list;
		} else if (Math.abs(dx - targetx) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new VerticalLeftAlignAnchor(target);
			Quartet trio = new Quartet(targetx, targetx + dropAdapter.getHotspotPoint().x, miny, maxb, a);
			list.add(trio);
			return list;
		} else if (Math.abs(dx - targeti) < THRESHOLD_DISTANCE && (Math.abs(thisy - sr) < THRESHOLD_DISTANCE || Math.abs(thisy - su) < THRESHOLD_DISTANCE)) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new VerticalIndentAnchor(target);
			Quartet trio = new Quartet(targeti, targeti + dropAdapter.getHotspotPoint().x, miny, maxb, a);
			list.add(trio);
			return list;
		} else if (Math.abs(drx - targetr) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new VerticalRightAlignAnchor(target);
			Quartet trio = new Quartet(targetr, targetr - todrop.getWidth() + dropAdapter.getHotspotPoint().x, miny, maxb, a);
			list.add(trio);
			return list;
		} else if (Math.abs(drx - wr) < THRESHOLD_DISTANCE && thisy <= targetb && thisb >= targety) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new VerticalLeftRelatedAnchor(target);
			Quartet trio = new Quartet(wr, wr - todrop.getWidth() + dropAdapter.getHotspotPoint().x, miny, maxb, a);
			list.add(trio);
			return list;
		} else if (Math.abs(drx - wu) < THRESHOLD_DISTANCE && thisy <= targetb && thisb >= targety) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new VerticalLeftRelatedAnchor(target);
			Quartet trio = new Quartet(wr, wr - todrop.getWidth() + dropAdapter.getHotspotPoint().x, miny, maxb, a);
			list.add(trio);
			a = new VerticalLeftUnrelatedAnchor(target);
			trio = new Quartet(wu, wu - todrop.getWidth() + dropAdapter.getHotspotPoint().x, miny, maxb, a);
			list.add(trio);
			return list;
		} else if (Math.abs(drx - wm) < THRESHOLD_DISTANCE && thisy <= targetb && thisb >= targety) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new VerticalLeftRelatedAnchor(target);
			Quartet trio = new Quartet(wr, wr - todrop.getWidth() + dropAdapter.getHotspotPoint().x, miny, maxb, a);
			list.add(trio);
			a = new VerticalLeftUnrelatedAnchor(target);
			trio = new Quartet(wu, wu - todrop.getWidth() + dropAdapter.getHotspotPoint().x, miny, maxb, a);
			list.add(trio);
			a = new VerticalLeftLargeAnchor(target);
			trio = new Quartet(wm, wm - todrop.getWidth() + dropAdapter.getHotspotPoint().x, miny, maxb, a);
			list.add(trio);
			return list;
		} else if (Math.abs(dx - er) < THRESHOLD_DISTANCE && thisy <= targetb && thisb >= targety) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new VerticalRightRelatedAnchor(target);
			Quartet trio = new Quartet(er, er + dropAdapter.getHotspotPoint().x, miny, maxb, a);
			list.add(trio);
			return list;
		} else if (Math.abs(dx - eu) < THRESHOLD_DISTANCE && thisy <= targetb && thisb >= targety) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new VerticalRightRelatedAnchor(target);
			Quartet trio = new Quartet(er, er + dropAdapter.getHotspotPoint().x, miny, maxb, a);
			list.add(trio);
			a = new VerticalRightUnrelatedAnchor(target);
			trio = new Quartet(eu, eu + dropAdapter.getHotspotPoint().x, miny, maxb, a);
			list.add(trio);
			return list;
		} else if (Math.abs(dx - em) < THRESHOLD_DISTANCE && thisy <= targetb && thisb >= targety) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new VerticalRightRelatedAnchor(target);
			Quartet trio = new Quartet(er, er + dropAdapter.getHotspotPoint().x, miny, maxb, a);
			list.add(trio);
			a = new VerticalRightUnrelatedAnchor(target);
			trio = new Quartet(eu, eu + dropAdapter.getHotspotPoint().x, miny, maxb, a);
			list.add(trio);
			a = new VerticalRightLargeAnchor(target);
			trio = new Quartet(em, em + dropAdapter.getHotspotPoint().x, miny, maxb, a);
			list.add(trio);
			return list;
		}
		return null;
	}

	private List<Quartet> getHAnchor(JComponent todrop, JComponent target, Point this_point) {
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
		int tb = targetAdapter.getBaseline();
		int sb = dropAdapter.getBaseline();
		int dy = this_point.y - dropAdapter.getHotspotPoint().y;
		int dby = dy + todrop.getHeight();
		tb = ty + tb;
		sb = dy + sb;
		int thisx = this_point.x - dropAdapter.getHotspotPoint().x;
		int thisr = thisx + todrop.getWidth();
		int targetx = target.getX();
		int targetr = targetx + target.getWidth();
		int targety = target.getY();
		int targetb = targety + target.getHeight();
		int minx = Math.min(thisx, targetx) - ANCHOR_EXT;
		int maxr = Math.max(thisr, targetr) + ANCHOR_EXT;
		if (Math.abs(tb - sb) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new HorizontalBaselineAnchor(target);
			Quartet trio = new Quartet(tb, tb - dropAdapter.getBaseline() + dropAdapter.getHotspotPoint().y, minx, maxr, a);
			list.add(trio);
			return list;
		} else if (Math.abs(dy - targety) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new HorizontalTopAlignAnchor(target);
			Quartet trio = new Quartet(targety, targety + dropAdapter.getHotspotPoint().y, minx, maxr, a);
			list.add(trio);
			return list;
		} else if (Math.abs(dby - targetb) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new HorizontalBottomAlignAnchor(target);
			Quartet trio = new Quartet(targetb, targetb - todrop.getHeight() + dropAdapter.getHotspotPoint().y, minx, maxr, a);
			list.add(trio);
			return list;
		} else if (Math.abs(dby - nr) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new HorizontalTopRelatedAnchor(target);
			Quartet trio = new Quartet(nr, nr - todrop.getHeight() + dropAdapter.getHotspotPoint().y, minx, maxr, a);
			list.add(trio);
			return list;
		} else if (Math.abs(dby - nu) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new HorizontalTopRelatedAnchor(target);
			Quartet trio = new Quartet(nr, nr - todrop.getHeight() + dropAdapter.getHotspotPoint().y, minx, maxr, a);
			list.add(trio);
			a = new HorizontalTopUnrelatedAnchor(target);
			trio = new Quartet(nu, nu - todrop.getHeight() + dropAdapter.getHotspotPoint().y, minx, maxr, a);
			list.add(trio);
			return list;
		} else if (Math.abs(dby - nm) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new HorizontalTopRelatedAnchor(target);
			Quartet trio = new Quartet(nr, nr - todrop.getHeight() + dropAdapter.getHotspotPoint().y, minx, maxr, a);
			list.add(trio);
			a = new HorizontalTopUnrelatedAnchor(target);
			trio = new Quartet(nu, nu - todrop.getHeight() + dropAdapter.getHotspotPoint().y, minx, maxr, a);
			list.add(trio);
			a = new HorizontalTopLargeAnchor(target);
			trio = new Quartet(nm, nm - todrop.getHeight() + dropAdapter.getHotspotPoint().y, minx, maxr, a);
			list.add(trio);
			return list;
		} else if (Math.abs(dy - sr) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new HorizontalBottomRelatedAnchor(target);
			Quartet trio = new Quartet(sr, sr + dropAdapter.getHotspotPoint().y, minx, maxr, a);
			list.add(trio);
			return list;
		} else if (Math.abs(dy - su) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new HorizontalBottomRelatedAnchor(target);
			Quartet trio = new Quartet(sr, sr + dropAdapter.getHotspotPoint().y, minx, maxr, a);
			list.add(trio);
			a = new HorizontalBottomUnrelatedAnchor(target);
			trio = new Quartet(su, su + dropAdapter.getHotspotPoint().y, minx, maxr, a);
			list.add(trio);
			return list;
		} else if (Math.abs(dy - sm) < THRESHOLD_DISTANCE) {
			List<Quartet> list = new ArrayList<Quartet>();
			Anchor a = new HorizontalBottomRelatedAnchor(target);
			Quartet trio = new Quartet(sr, sr + dropAdapter.getHotspotPoint().y, minx, maxr, a);
			list.add(trio);
			a = new HorizontalBottomUnrelatedAnchor(target);
			trio = new Quartet(su, su + dropAdapter.getHotspotPoint().y, minx, maxr, a);
			list.add(trio);
			a = new HorizontalBottomLargeAnchor(target);
			trio = new Quartet(sm, sm + dropAdapter.getHotspotPoint().y, minx, maxr, a);
			list.add(trio);
			return list;
		}
		return null;
	}

	protected int getAzimuth(Point this_point, Point last_point) {
		if (this_point.x == last_point.x) {
			if (this_point.y <= last_point.y)
				return NORTH;
			else
				return SOUTH;
		} else if (this_point.x > last_point.x) {
			if (this_point.y < last_point.y) {
				return NORTH_EAST;
			} else if (this_point.y == last_point.y) {
				return EAST;
			} else {
				return SOUTH_EAST;
			}
		} else {
			if (this_point.y < last_point.y) {
				return NORTH_WEST;
			} else if (this_point.y == last_point.y) {
				return WEST;
			} else {
				return SOUTH_WEST;
			}
		}
	}

	protected Quartet calMasc(int masc, List<Quartet> anchor) {
		Quartet res = null;
		int min = Integer.MAX_VALUE;
		for (Quartet trio : anchor) {
			int another = Math.abs(masc - trio.axis);
			if (min > another) {
				min = another;
				res = trio;
			}
		}
		return res;
	}

}
