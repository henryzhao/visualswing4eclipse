/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.grouplayout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.dyno.visual.swing.base.Azimuth;
import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Spring;
import org.dyno.visual.swing.layouts.Trailing;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.ILayoutBean;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class GroupLayoutAdapter extends LayoutAdapter implements ILayoutBean {
	private static Color BASELINE_COLOR = new Color(143, 171, 196);
	private static Stroke STROKE1 = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 4, 2 }, 0);
    private static Stroke STROKE2 = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 1, 1 }, 0);
	private boolean hovered;
	private List<Quartet> horizontal_baseline;
	private List<Quartet> vertical_baseline;

	private Constraints last_constraints;

	private IDragOperation beanHover;
	private IDragOperation resizeLeft;
	private IDragOperation resizeLeftTop;
	private IDragOperation resizeTop;
	private IDragOperation resizeRightTop;
	private IDragOperation resizeRight;
	private IDragOperation resizeRightBottom;
	private IDragOperation resizeBottom;
	private IDragOperation resizeLeftBottom;

	@Override
	public void setContainer(JComponent container) {
		super.setContainer(container);
		GroupLayout layout = (GroupLayout) container.getLayout();
		beanHover = new BeanHover(this, layout, container);
		resizeRightBottom = new ResizeRightBottom(this, layout, container);
		resizeLeftTop = new ResizeLeftTop(this, layout, container);
		resizeLeftBottom = new ResizeLeftBottom(this, layout, container);
		resizeRightTop = new ResizeRightTop(this, layout, container);
		resizeLeft = new ResizeLeft(this, layout, container);
		resizeTop = new ResizeTop(this, layout, container);
		resizeRight = new ResizeRight(this, layout, container);
		resizeBottom = new ResizeBottom(this, layout, container);
		hovered = false;
		horizontal_baseline = null;
		vertical_baseline = null;
	}

	Constraints getLastConstraints() {
		return last_constraints;
	}

	@Override
	public void addChild(JComponent widget) {
		Constraints cons = new Constraints(new Leading(10, 10, 10),new Leading(10, 10, 10));		
		container.add(widget, cons);
	}

	void setBaseline(List<Quartet> hList, List<Quartet> vList) {
		horizontal_baseline = hList;
		vertical_baseline = vList;
	}

	void setHovered(boolean hovered) {
		this.hovered = hovered;
	}
	private static final int BOX = 5;
	@Override
	public void paintFocused(Graphics g) {
	}

	@Override
	public void paintBaselineAnchor(Graphics g) {
		if (hovered) {
			Graphics2D g2d = (Graphics2D) g;
			Stroke old = g2d.getStroke();
			g2d.setColor(BASELINE_COLOR);
			g2d.setStroke(STROKE1);
			if (horizontal_baseline != null) {
				for (Quartet trio : horizontal_baseline) {
					g2d.drawLine(trio.start, trio.axis, trio.end, trio.axis);
				}
			}
			if (vertical_baseline != null) {
				for (Quartet trio : vertical_baseline) {
					g2d.drawLine(trio.axis, trio.start, trio.axis, trio.end);
				}
			}
			g2d.setStroke(old);
		} else {
			Graphics2D g2d = (Graphics2D) g;
			Stroke old = g2d.getStroke();
			g2d.setColor(BASELINE_COLOR);
			g2d.setStroke(STROKE2);
			GroupLayout layout = (GroupLayout) container.getLayout();
			int width = container.getWidth();
			int height = container.getHeight();
			CompositeAdapter adapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
			int count = adapter.getChildCount();
			for (int i = 0; i < count; i++) {
				JComponent child = adapter.getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
				if (childAdapter.isSelected()) {
					Constraints constraints = layout.getConstraints(child);
					Alignment horizontal = constraints.getHorizontal();
					int y = child.getY() + child.getHeight() / 2;
					int x = 0;
					if (horizontal instanceof Leading) {
						x = ((Leading) horizontal).getLeading();
						g2d.drawLine(0, y, x, y);
						Polygon polygon = new Polygon();
						polygon.addPoint(0, y-BOX+1);
						polygon.addPoint(0, y+BOX);
						polygon.addPoint(BOX, y);
						g2d.fillPolygon(polygon);
					} else if (horizontal instanceof Trailing) {
						x = width - ((Trailing) horizontal).getTrailing();
						g2d.drawLine(width, y, x, y);
						Polygon polygon = new Polygon();
						polygon.addPoint(width, y-BOX);
						polygon.addPoint(width, y+BOX-1);
						polygon.addPoint(width - BOX, y);
						g2d.fillPolygon(polygon);
					} else if (horizontal instanceof Bilateral) {
						x = ((Bilateral) horizontal).getLeading();
						g2d.drawLine(0, y, x, y);
						Polygon polygon = new Polygon();
						polygon.addPoint(0, y-BOX+1);
						polygon.addPoint(0, y+BOX);
						polygon.addPoint(BOX, y);
						g2d.fillPolygon(polygon);
						x = width - ((Bilateral) horizontal).getTrailing();
						g2d.drawLine(width, y, x, y);
						polygon = new Polygon();
						polygon.addPoint(width, y-BOX);
						polygon.addPoint(width, y+BOX-1);
						polygon.addPoint(width - BOX, y);
						g2d.fillPolygon(polygon);
					}
					Alignment vertical = constraints.getVertical();
					x = child.getX() + child.getWidth() / 2;
					if (vertical instanceof Leading) {
						y = ((Leading) vertical).getLeading();
						g2d.drawLine(x, 0, x, y);
						Polygon polygon = new Polygon();
						polygon.addPoint(x-BOX+1, 0);
						polygon.addPoint(x+BOX, 0);
						polygon.addPoint(x, BOX);
						g2d.fillPolygon(polygon);
					} else if (vertical instanceof Trailing) {
						y = height - ((Trailing) vertical).getTrailing();
						g2d.drawLine(x, height, x, y);
						Polygon polygon = new Polygon();
						polygon.addPoint(x-BOX, height);
						polygon.addPoint(x+BOX-1, height);
						polygon.addPoint(x, height-BOX);
						g2d.fillPolygon(polygon);
					} else if (vertical instanceof Bilateral) {
						y = ((Bilateral) vertical).getLeading();
						g2d.drawLine(x, 0, x, y);
						Polygon polygon = new Polygon();
						polygon.addPoint(x-BOX+1, 0);
						polygon.addPoint(x+BOX, 0);
						polygon.addPoint(x, BOX);
						g2d.fillPolygon(polygon);
						y = height - ((Bilateral) vertical).getTrailing();
						g2d.drawLine(x, height, x, y);
						polygon = new Polygon();
						polygon.addPoint(x-BOX, height);
						polygon.addPoint(x+BOX-1, height);
						polygon.addPoint(x, height-BOX);
						g2d.fillPolygon(polygon);
					}
				}
			}
			g2d.setStroke(old);
		}
	}

	@Override
	public boolean dragEnter(Point p) {
		hovered = true;
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int state = parent.getState();
		switch (state) {
		case Azimuth.STATE_BEAN_HOVER:
			return beanHover.dragEnter(p);
		case Azimuth.STATE_BEAN_RESIZE_LEFT:
			return resizeLeft.dragEnter(p);
		case Azimuth.STATE_BEAN_RESIZE_LEFT_TOP:
			return resizeLeftTop.dragEnter(p);
		case Azimuth.STATE_BEAN_RESIZE_TOP:
			return resizeTop.dragEnter(p);
		case Azimuth.STATE_BEAN_RESIZE_RIGHT_TOP:
			return resizeRightTop.dragEnter(p);
		case Azimuth.STATE_BEAN_RESIZE_RIGHT:
			return resizeRight.dragEnter(p);
		case Azimuth.STATE_BEAN_RESIZE_RIGHT_BOTTOM:
			return resizeRightBottom.dragEnter(p);
		case Azimuth.STATE_BEAN_RESIZE_BOTTOM:
			return resizeBottom.dragEnter(p);
		case Azimuth.STATE_BEAN_RESIZE_LEFT_BOTTOM:
			return resizeLeftBottom.dragEnter(p);
		default:
			parent.setMascotLocation(p);
			return true;
		}
	}

	@Override
	public boolean dragExit(Point p) {
		hovered = false;
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int state = parent.getState();
		switch (state) {
		case Azimuth.STATE_BEAN_HOVER:
			return beanHover.dragExit(p);
		case Azimuth.STATE_BEAN_RESIZE_LEFT:
			return resizeLeft.dragExit(p);
		case Azimuth.STATE_BEAN_RESIZE_LEFT_TOP:
			return resizeLeftTop.dragExit(p);
		case Azimuth.STATE_BEAN_RESIZE_TOP:
			return resizeTop.dragExit(p);
		case Azimuth.STATE_BEAN_RESIZE_RIGHT_TOP:
			return resizeRightTop.dragExit(p);
		case Azimuth.STATE_BEAN_RESIZE_RIGHT:
			return resizeRight.dragExit(p);
		case Azimuth.STATE_BEAN_RESIZE_RIGHT_BOTTOM:
			return resizeRightBottom.dragExit(p);
		case Azimuth.STATE_BEAN_RESIZE_BOTTOM:
			return resizeBottom.dragExit(p);
		case Azimuth.STATE_BEAN_RESIZE_LEFT_BOTTOM:
			return resizeLeftBottom.dragExit(p);
		default:
			parent.setMascotLocation(p);
			return true;
		}
	}

	@Override
	public boolean dragOver(Point p) {
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int state = parent.getState();
		switch (state) {
		case Azimuth.STATE_BEAN_HOVER:
			return beanHover.dragOver(p);
		case Azimuth.STATE_BEAN_RESIZE_LEFT:
			return resizeLeft.dragOver(p);
		case Azimuth.STATE_BEAN_RESIZE_LEFT_TOP:
			return resizeLeftTop.dragOver(p);
		case Azimuth.STATE_BEAN_RESIZE_TOP:
			return resizeTop.dragOver(p);
		case Azimuth.STATE_BEAN_RESIZE_RIGHT_TOP:
			return resizeRightTop.dragOver(p);
		case Azimuth.STATE_BEAN_RESIZE_RIGHT:
			return resizeRight.dragOver(p);
		case Azimuth.STATE_BEAN_RESIZE_RIGHT_BOTTOM:
			return resizeRightBottom.dragOver(p);
		case Azimuth.STATE_BEAN_RESIZE_BOTTOM:
			return resizeBottom.dragOver(p);
		case Azimuth.STATE_BEAN_RESIZE_LEFT_BOTTOM:
			return resizeLeftBottom.dragOver(p);
		default:
			return false;
		}
	}

	@Override
	public boolean drop(Point p) {
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int state = parent.getState();
		switch (state) {
		case Azimuth.STATE_BEAN_HOVER:
			hovered = false;
			return beanHover.drop(p);
		case Azimuth.STATE_BEAN_RESIZE_LEFT:
			return resizeLeft.drop(p);
		case Azimuth.STATE_BEAN_RESIZE_LEFT_TOP:
			return resizeLeftTop.drop(p);
		case Azimuth.STATE_BEAN_RESIZE_TOP:
			return resizeTop.drop(p);
		case Azimuth.STATE_BEAN_RESIZE_RIGHT_TOP:
			return resizeRightTop.drop(p);
		case Azimuth.STATE_BEAN_RESIZE_RIGHT:
			return resizeRight.drop(p);
		case Azimuth.STATE_BEAN_RESIZE_RIGHT_BOTTOM:
			return resizeRightBottom.drop(p);
		case Azimuth.STATE_BEAN_RESIZE_BOTTOM:
			return resizeBottom.drop(p);
		case Azimuth.STATE_BEAN_RESIZE_LEFT_BOTTOM:
			return resizeLeftBottom.drop(p);
		}
		return false;
	}

	@Override
	public boolean isChildResizable() {
		return true;
	}

	@Override
	public boolean removeChild(JComponent child) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		last_constraints = layout.getConstraints(child);
		assert last_constraints != null;
		container.remove(child);
		container.validate();
		return true;
	}

	@Override
	public void initConainerLayout(Container container) {
		int count = container.getComponentCount();
		HashMap<JComponent, Constraints> comps = new HashMap<JComponent, Constraints>();
		ArrayList<JComponent> array = new ArrayList<JComponent>();
		LayoutStyle style = LayoutStyle.getInstance();
		for (int i = 0; i < count; i++) {
			JComponent widget = (JComponent) container.getComponent(i);
			int gap = style.getContainerGap(widget, SwingConstants.EAST, container);
			Rectangle bounds = widget.getBounds();
			Spring spring = new Spring(gap, gap);
			Leading horizontal = new Leading(bounds.x, bounds.width, spring);
			gap = style.getContainerGap(widget, SwingConstants.SOUTH, container);
			spring = new Spring(gap, gap);
			Leading vertical = new Leading(bounds.y, bounds.height, spring);
			Constraints constraints = new Constraints(horizontal, vertical);
			comps.put(widget, constraints);
			array.add(widget);
		}
		container.removeAll();
		GroupLayout layout = new GroupLayout();
		container.setLayout(layout);
		for (JComponent widget : array) {
			Constraints constraints = comps.get(widget);
			container.add(widget, constraints);
		}
		container.doLayout();
		container.validate();
	}

	@Override
	public boolean cloneLayout(JComponent panel) {
		panel.setLayout(copyLayout(panel));
		GroupLayout layout = (GroupLayout) container.getLayout();
		int count = container.getComponentCount();
		for (int i = 0; i < count; i++) {
			JComponent child = (JComponent) container.getComponent(i);
			WidgetAdapter cAdapter = WidgetAdapter.getWidgetAdapter(child);
			panel.add(cAdapter.cloneWidget(), layout.getConstraints(child));
		}
		return true;
	}

	@Override
	protected LayoutManager copyLayout(Container con) {
		return new GroupLayout();
	}

	@Override
	public void adjustLayout(JComponent widget) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		Constraints constraints = layout.getConstraints(widget);
		Alignment horizontal = constraints.getHorizontal();
		if (horizontal instanceof Leading) {
			Leading leading = (Leading) horizontal;
			int now_width = widget.getWidth();
			int pref_width = widget.getPreferredSize().width;
			int delta_width = pref_width - now_width;
			if (delta_width != 0) {
				leading.setSize(Alignment.PREFERRED);
				adjustHorizontalLeadingBy(widget, delta_width);
				container.doLayout();
				container.validate();
			}
		} else if (horizontal instanceof Trailing) {
			Trailing trailing = (Trailing) horizontal;
			int now_width = widget.getWidth();
			int pref_width = widget.getPreferredSize().width;
			int delta_width = pref_width - now_width;
			if (delta_width != 0) {
				trailing.setSize(Alignment.PREFERRED);
				adjustHorizontalTrailingBy(widget, delta_width);
				container.doLayout();
				container.validate();
			}
		}

		Alignment vertical = constraints.getVertical();
		if (vertical instanceof Leading) {
			Leading leading = (Leading) vertical;
			int now_height = widget.getHeight();
			int pref_height = widget.getPreferredSize().height;
			int delta_height = pref_height - now_height;
			if (delta_height != 0) {
				leading.setSize(Alignment.PREFERRED);
				adjustVerticalLeadingBy(widget, delta_height);
				container.doLayout();
				container.validate();
			}
		} else if (vertical instanceof Trailing) {
			Trailing trailing = (Trailing) vertical;
			int now_height = widget.getHeight();
			int pref_height = widget.getPreferredSize().height;
			int delta_height = pref_height - now_height;
			if (delta_height != 0) {
				trailing.setSize(Alignment.PREFERRED);
				adjustVerticalTrailingBy(widget, delta_height);
				container.doLayout();
				container.validate();
			}
		}
	}

	private void adjustVerticalTrailingBy(JComponent widget, int delta_height) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int count = containerAdapter.getChildCount();
		for (int i = 0; i < count; i++) {
			JComponent target = containerAdapter.getChild(i);
			if (target != widget) {
				if (isTopRelatedTo(target, widget)) {
					Constraints constraints = layout.getConstraints(target);
					Alignment vertical = constraints.getHorizontal();
					if (vertical instanceof Trailing) {
						Trailing leading = (Trailing) vertical;
						leading.setTrailing(leading.getTrailing() + delta_height);
						adjustVerticalTrailingBy(target, delta_height);
					} else if (vertical instanceof Bilateral) {
						Bilateral spring = (Bilateral) vertical;
						spring.setTrailing(spring.getTrailing() + delta_height);
					}
				}
			}
		}
	}

	private boolean isTopRelatedTo(JComponent target, JComponent widget) {
		LayoutStyle style = LayoutStyle.getInstance();
		int nr = style.getPreferredGap(target, widget, ComponentPlacement.RELATED, SwingConstants.NORTH, container);
		int wr = style.getPreferredGap(target, widget, ComponentPlacement.RELATED, SwingConstants.WEST, container);
		int er = style.getPreferredGap(target, widget, ComponentPlacement.RELATED, SwingConstants.EAST, container);
		Rectangle tgtb = target.getBounds();
		Rectangle srcb = widget.getBounds();
		int tgtr = tgtb.y + tgtb.height;
		int srcy = srcb.y;
		if (srcy > tgtr) {
			int distance = srcy - tgtr;
			if (distance == nr && tgtb.x + tgtb.width >= srcb.x - wr && tgtb.x <= srcb.x + srcb.width + er)
				return true;
		}
		return false;
	}

	private void adjustVerticalLeadingBy(JComponent widget, int delta_height) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int count = containerAdapter.getChildCount();
		for (int i = 0; i < count; i++) {
			JComponent target = containerAdapter.getChild(i);
			if (target != widget) {
				if (isBottomRelatedTo(target, widget)) {
					Constraints constraints = layout.getConstraints(target);
					Alignment vertical = constraints.getVertical();
					if (vertical instanceof Leading) {
						Leading leading = (Leading) vertical;
						leading.setLeading(leading.getLeading() + delta_height);
						adjustVerticalLeadingBy(target, delta_height);
					} else if (vertical instanceof Bilateral) {
						Bilateral spring = (Bilateral) vertical;
						spring.setLeading(spring.getLeading() + delta_height);
					}
				}
			}
		}
	}

	private boolean isBottomRelatedTo(JComponent target, JComponent widget) {
		LayoutStyle style = LayoutStyle.getInstance();
		int sr = style.getPreferredGap(target, widget, ComponentPlacement.RELATED, SwingConstants.SOUTH, container);
		int wr = style.getPreferredGap(target, widget, ComponentPlacement.RELATED, SwingConstants.WEST, container);
		int er = style.getPreferredGap(target, widget, ComponentPlacement.RELATED, SwingConstants.EAST, container);
		Rectangle tgtb = target.getBounds();
		Rectangle srcb = widget.getBounds();
		int tgty = tgtb.y;
		int srcr = srcb.y + srcb.height;
		if (tgty > srcr) {
			int distance = tgty - srcr;
			if (distance == sr && tgtb.x + tgtb.width >= srcb.x - wr && tgtb.x <= srcb.x + srcb.width + er)
				return true;
		}
		return false;
	}

	private void adjustHorizontalTrailingBy(JComponent widget, int delta_width) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int count = containerAdapter.getChildCount();
		for (int i = 0; i < count; i++) {
			JComponent target = containerAdapter.getChild(i);
			if (target != widget) {
				if (isLeftRelatedTo(target, widget)) {
					Constraints constraints = layout.getConstraints(target);
					Alignment horizontal = constraints.getHorizontal();
					if (horizontal instanceof Trailing) {
						Trailing leading = (Trailing) horizontal;
						leading.setTrailing(leading.getTrailing() + delta_width);
						adjustHorizontalTrailingBy(target, delta_width);
					} else if (horizontal instanceof Bilateral) {
						Bilateral spring = (Bilateral) horizontal;
						spring.setTrailing(spring.getTrailing() + delta_width);
					}
				}
			}
		}
	}

	private boolean isLeftRelatedTo(JComponent target, JComponent widget) {
		LayoutStyle style = LayoutStyle.getInstance();
		int wr = style.getPreferredGap(target, widget, ComponentPlacement.RELATED, SwingConstants.WEST, container);
		int nr = style.getPreferredGap(target, widget, ComponentPlacement.RELATED, SwingConstants.NORTH, container);
		int sr = style.getPreferredGap(target, widget, ComponentPlacement.RELATED, SwingConstants.SOUTH, container);
		Rectangle tgtb = target.getBounds();
		Rectangle srcb = widget.getBounds();
		int tgtr = tgtb.x + tgtb.width;
		int srcx = srcb.x;
		if (srcx > tgtr) {
			int distance = srcx - tgtr;
			if (distance == wr && tgtb.y + tgtb.height >= srcb.y - nr && tgtb.y <= srcb.y + srcb.height + sr)
				return true;
		}
		return false;
	}

	private void adjustHorizontalLeadingBy(JComponent widget, int delta_width) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int count = containerAdapter.getChildCount();
		for (int i = 0; i < count; i++) {
			JComponent target = containerAdapter.getChild(i);
			if (target != widget) {
				if (isRightRelatedTo(target, widget)) {
					Constraints constraints = layout.getConstraints(target);
					Alignment horizontal = constraints.getHorizontal();
					if (horizontal instanceof Leading) {
						Leading leading = (Leading) horizontal;
						leading.setLeading(leading.getLeading() + delta_width);
						adjustHorizontalLeadingBy(target, delta_width);
					} else if (horizontal instanceof Bilateral) {
						Bilateral spring = (Bilateral) horizontal;
						spring.setLeading(spring.getLeading() + delta_width);
					}
				}
			}
		}
	}

	private boolean isRightRelatedTo(JComponent target, JComponent widget) {
		LayoutStyle style = LayoutStyle.getInstance();
		int er = style.getPreferredGap(target, widget, ComponentPlacement.RELATED, SwingConstants.EAST, container);
		int nr = style.getPreferredGap(target, widget, ComponentPlacement.RELATED, SwingConstants.NORTH, container);
		int sr = style.getPreferredGap(target, widget, ComponentPlacement.RELATED, SwingConstants.SOUTH, container);
		Rectangle tgtb = target.getBounds();
		Rectangle srcb = widget.getBounds();
		int tgtx = tgtb.x;
		int srcr = srcb.x + srcb.width;
		if (tgtx > srcr) {
			int distance = tgtx - srcr;
			if (distance == er && tgtb.y + tgtb.height >= srcb.y - nr && tgtb.y <= srcb.y + srcb.height + sr)
				return true;
		}
		return false;
	}

	@Override
	public boolean isSelectionAlignResize(String id) {
		if (id == null)
			return false;
		GroupLayout layout = (GroupLayout) container.getLayout();
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int count = containerAdapter.getChildCount();
		boolean resizable = false;
		for (int i = 0; i < count; i++) {
			JComponent target = containerAdapter.getChild(i);
			WidgetAdapter targetAdapter = WidgetAdapter.getWidgetAdapter(target);
			if (targetAdapter.isSelected()) {
				Constraints constraints = layout.getConstraints(target);
				Alignment horizontal = constraints.getHorizontal();
				Alignment vertical = constraints.getVertical();
				if (id.equals("same_height")) {
					if (vertical instanceof Leading) {
						resizable = true;
					} else if (vertical instanceof Bilateral) {
						return false;
					} else if (vertical instanceof Trailing) {
						resizable = true;
					}
				} else if (id.equals("same_width")) {
					if (horizontal instanceof Leading) {
						resizable = true;
					} else if (horizontal instanceof Bilateral) {
						return false;
					} else if (horizontal instanceof Trailing) {
						resizable = true;
					}
				} else if (id.equals("middle")) {
					if (vertical instanceof Leading) {
						resizable = true;
					} else if (vertical instanceof Bilateral) {
						return false;
					} else if (vertical instanceof Trailing) {
						resizable = true;
					}
				} else if (id.equals("center")) {
					if (horizontal instanceof Leading) {
						resizable = true;
					} else if (horizontal instanceof Bilateral) {
						return false;
					} else if (horizontal instanceof Trailing) {
						resizable = true;
					}
				} else if (id.equals("bottom")) {
					if (vertical instanceof Leading) {
						resizable = true;
					} else if (vertical instanceof Bilateral) {
						return false;
					} else if (vertical instanceof Trailing) {
						resizable = true;
					}
				} else if (id.equals("left")) {
					if (horizontal instanceof Leading) {
						resizable = true;
					} else if (horizontal instanceof Bilateral) {
						return false;
					} else if (horizontal instanceof Trailing) {
						resizable = true;
					}
				} else if (id.equals("right")) {
					if (horizontal instanceof Leading) {
						resizable = true;
					} else if (horizontal instanceof Bilateral) {
						return false;
					} else if (horizontal instanceof Trailing) {
						resizable = true;
					}
				} else if (id.equals("top")) {
					if (vertical instanceof Leading) {
						resizable = true;
					} else if (vertical instanceof Bilateral) {
						return false;
					} else if (vertical instanceof Trailing) {
						resizable = true;
					}
				}
			}
		}
		return resizable;
	}

	public boolean doAlignment(String id) {
		return false;
	}

	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		String name = imports.addImport("org.dyno.visual.swing.layouts.GroupLayout");
		return "new " + name + "()";
	}

	@Override
	protected String getChildConstraints(JComponent child, ImportRewrite imports) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		Constraints constraints = layout.getConstraints(child);
		StringBuilder builder = new StringBuilder();
		String strConstraints = imports.addImport("org.dyno.visual.swing.layouts.Constraints");
		builder.append("new " + strConstraints + "(");
		genAlignmentCode(builder, constraints.getHorizontal(), imports);
		builder.append(", ");
		genAlignmentCode(builder, constraints.getVertical(), imports);
		builder.append(")");
		return builder.toString();
	}

	private void genAlignmentCode(StringBuilder builder, Alignment alignment, ImportRewrite imports) {
		if (alignment instanceof Leading) {
			genLeadingCode(builder, (Leading) alignment, imports);
		} else if (alignment instanceof Trailing) {
			genTrailingCode(builder, (Trailing) alignment, imports);
		} else if (alignment instanceof Bilateral) {
			genBilateralCode(builder, (Bilateral) alignment, imports);
		}
	}

	private void genBilateralCode(StringBuilder builder, Bilateral bilateral, ImportRewrite imports) {
		String strBilateral = imports.addImport("org.dyno.visual.swing.layouts.Bilateral");
		builder.append("new " + strBilateral + "(");
		builder.append(bilateral.getLeading() + ", ");
		builder.append(bilateral.getTrailing() + ", ");
		builder.append(bilateral.getSpring().getMinimum());
		int pref = bilateral.getSpring().getPreferred();
		if (pref != Alignment.PREFERRED) {
			builder.append(", ");
			builder.append(pref);
		}
		builder.append(")");
	}

	private void genTrailingCode(StringBuilder builder, Trailing trailing, ImportRewrite imports) {
		String strTrailing = imports.addImport("org.dyno.visual.swing.layouts.Trailing");
		builder.append("new " + strTrailing + "(");
		builder.append(trailing.getTrailing() + ", ");
		int size = trailing.getSize();
		if (size != Alignment.PREFERRED) {
			builder.append(size);
			builder.append(", ");
		}
		builder.append(trailing.getSpring().getMinimum() + ", ");
		builder.append(trailing.getSpring().getPreferred());
		builder.append(")");
	}

	private void genLeadingCode(StringBuilder builder, Leading leading, ImportRewrite imports) {
		String strLeading = imports.addImport("org.dyno.visual.swing.layouts.Leading");
		builder.append("new " + strLeading + "(");
		builder.append(leading.getLeading() + ", ");
		int size = leading.getSize();
		if (size != Alignment.PREFERRED) {
			builder.append(size);
			builder.append(", ");
		}
		builder.append(leading.getSpring().getMinimum() + ", ");
		builder.append(leading.getSpring().getPreferred());
		builder.append(")");
	}

	@Override
	public void addChildByConstraints(JComponent child, Object constraints) {
		container.add(child, (Constraints)constraints);
	}

	@Override
	public Object getChildConstraints(JComponent child) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		Constraints constraints = layout.getConstraints(child);
		return constraints;
	}
}
