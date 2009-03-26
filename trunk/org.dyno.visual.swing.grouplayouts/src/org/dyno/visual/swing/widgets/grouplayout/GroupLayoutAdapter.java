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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
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
import org.dyno.visual.swing.base.JavaUtil;
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
import org.dyno.visual.swing.widgets.grouplayout.undo.BottomAlignmentOperation;
import org.dyno.visual.swing.widgets.grouplayout.undo.CenterAlignmentOperation;
import org.dyno.visual.swing.widgets.grouplayout.undo.LeftAlignmentOperation;
import org.dyno.visual.swing.widgets.grouplayout.undo.MiddleAlignmentOperation;
import org.dyno.visual.swing.widgets.grouplayout.undo.RightAlignmentOperation;
import org.dyno.visual.swing.widgets.grouplayout.undo.SameHeightOperation;
import org.dyno.visual.swing.widgets.grouplayout.undo.SameWidthOperation;
import org.dyno.visual.swing.widgets.grouplayout.undo.TopAlignmentOperation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.PlatformUI;

public class GroupLayoutAdapter extends LayoutAdapter implements ILayoutBean {
	private static Color BASELINE_COLOR = new Color(143, 171, 196);
	private static Stroke STROKE1 = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 4, 2 }, 0);
	private static Stroke STROKE2 = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 1, 1 }, 0);
	private boolean hovered;
	private List<Quartet> horizontal_baseline;
	private List<Quartet> vertical_baseline;

	private Constraints last_constraints;

	private List<IDragOperation> beanHover;
	private List<IDragOperation> resizeLeft;
	private List<IDragOperation> resizeLeftTop;
	private List<IDragOperation> resizeTop;
	private List<IDragOperation> resizeRightTop;
	private List<IDragOperation> resizeRight;
	private List<IDragOperation> resizeRightBottom;
	private List<IDragOperation> resizeBottom;
	private List<IDragOperation> resizeLeftBottom;

	@Override
	public void fillConstraintsAction(MenuManager menu, Component child) {
		MenuManager horizontalAnchorMenu = new MenuManager(Messages.GroupLayoutAdapter_Horizontal_Anchor, "#HORIZONTAL_ANCHOR"); //$NON-NLS-1$
		horizontalAnchorMenu.add(new SetAnchorAction(container, true, "leading", child)); //$NON-NLS-1$
		horizontalAnchorMenu.add(new SetAnchorAction(container, true, "bilateral", child)); //$NON-NLS-1$
		horizontalAnchorMenu.add(new SetAnchorAction(container, true, "trailing", child)); //$NON-NLS-1$
		menu.add(horizontalAnchorMenu);
		MenuManager verticalAnchorMenu = new MenuManager(Messages.GroupLayoutAdapter_Vertical_Anchor, "#VERTICAL_ANCHOR"); //$NON-NLS-1$
		verticalAnchorMenu.add(new SetAnchorAction(container, false, "leading", //$NON-NLS-1$
				child));
		verticalAnchorMenu.add(new SetAnchorAction(container, false, "bilateral", child)); //$NON-NLS-1$
		verticalAnchorMenu.add(new SetAnchorAction(container, false, "trailing", child)); //$NON-NLS-1$
		menu.add(verticalAnchorMenu);
	}

	private void initDragOperation(JComponent container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		beanHover = new ArrayList<IDragOperation>();
		resizeRightBottom = new ArrayList<IDragOperation>();
		resizeLeftTop = new ArrayList<IDragOperation>();
		resizeLeftBottom = new ArrayList<IDragOperation>();
		resizeRightTop = new ArrayList<IDragOperation>();
		resizeLeft = new ArrayList<IDragOperation>();
		resizeTop = new ArrayList<IDragOperation>();
		resizeRight = new ArrayList<IDragOperation>();
		resizeBottom = new ArrayList<IDragOperation>();
		WidgetAdapter parent = WidgetAdapter.getWidgetAdapter(container);
		for (WidgetAdapter todrop : parent.getDropWidget()) {
			beanHover.add(new BeanHover(this, todrop, layout, container));
			resizeRightBottom.add(new ResizeRightBottom(this, todrop, layout, container));
			resizeLeftTop.add(new ResizeLeftTop(this, todrop, layout, container));
			resizeLeftBottom.add(new ResizeLeftBottom(this, todrop, layout, container));
			resizeRightTop.add(new ResizeRightTop(this, todrop, layout, container));
			resizeLeft.add(new ResizeLeft(this, todrop, layout, container));
			resizeTop.add(new ResizeTop(this, todrop, layout, container));
			resizeRight.add(new ResizeRight(this, todrop, layout, container));
			resizeBottom.add(new ResizeBottom(this, todrop, layout, container));
		}
		hovered = false;
	}

	Constraints getLastConstraints() {
		return last_constraints;
	}

	@Override
	public void addChild(Component widget) {
		Constraints cons = new Constraints(new Leading(10, 10, 10), new Leading(10, 10, 10));
		container.add(widget, cons);
	}

	void addBaseline(List<Quartet> hList, List<Quartet> vList) {
		if (horizontal_baseline == null)
			horizontal_baseline = new ArrayList<Quartet>();
		if (vertical_baseline == null)
			vertical_baseline = new ArrayList<Quartet>();
		if (hList != null)
			horizontal_baseline.addAll(hList);
		if (vList != null)
			vertical_baseline.addAll(vList);
	}

	void setHovered(boolean hovered) {
		this.hovered = hovered;
	}

	private static final int BOX = 5;

	@Override
	public void paintHint(Graphics g) {
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
		}
	}

	@Override
	public void paintAnchor(Graphics g) {
		if (!hovered) {
			Graphics2D g2d = (Graphics2D) g;
			Stroke old = g2d.getStroke();
			g2d.setColor(BASELINE_COLOR);
			g2d.setStroke(STROKE2);
			GroupLayout layout = (GroupLayout) container.getLayout();
			int width = container.getWidth();
			int height = container.getHeight();
			Insets insets = container.getInsets();
			CompositeAdapter adapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
			int count = adapter.getChildCount();
			for (int i = 0; i < count; i++) {
				Component child = adapter.getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
				if (childAdapter.isSelected()) {
					Constraints constraints = layout.getConstraints(child);
					Alignment horizontal = constraints.getHorizontal();
					int y = child.getY() + child.getHeight() / 2;
					int x = 0;
					if (horizontal instanceof Leading) {
						x = ((Leading) horizontal).getLeading();
						g2d.drawLine(insets.left, y, insets.left + x, y);
						Polygon polygon = new Polygon();
						polygon.addPoint(insets.left, y - BOX + 1);
						polygon.addPoint(insets.left, y + BOX);
						polygon.addPoint(insets.left + BOX, y);
						g2d.fillPolygon(polygon);
					} else if (horizontal instanceof Trailing) {
						x = child.getX() + child.getWidth();
						g2d.drawLine(width - insets.right, y, x, y);
						Polygon polygon = new Polygon();
						polygon.addPoint(width - insets.right, y - BOX);
						polygon.addPoint(width - insets.right, y + BOX - 1);
						polygon.addPoint(width - BOX - insets.right, y);
						g2d.fillPolygon(polygon);
					} else if (horizontal instanceof Bilateral) {
						x = ((Bilateral) horizontal).getLeading();
						g2d.drawLine(insets.left, y, insets.left + x, y);
						Polygon polygon = new Polygon();
						polygon.addPoint(insets.left, y - BOX + 1);
						polygon.addPoint(insets.left, y + BOX);
						polygon.addPoint(insets.left + BOX, y);
						g2d.fillPolygon(polygon);
						x = child.getX() + child.getWidth();
						g2d.drawLine(width - insets.right, y, x, y);
						polygon = new Polygon();
						polygon.addPoint(width - insets.right, y - BOX);
						polygon.addPoint(width - insets.right, y + BOX - 1);
						polygon.addPoint(width - BOX - insets.right, y);
						g2d.fillPolygon(polygon);
					}
					Alignment vertical = constraints.getVertical();
					x = child.getX() + child.getWidth() / 2;
					if (vertical instanceof Leading) {
						y = ((Leading) vertical).getLeading();
						g2d.drawLine(x, insets.top, x, insets.top + y);
						Polygon polygon = new Polygon();
						polygon.addPoint(x - BOX + 1, insets.top);
						polygon.addPoint(x + BOX, insets.top);
						polygon.addPoint(x, BOX + insets.top);
						g2d.fillPolygon(polygon);
					} else if (vertical instanceof Trailing) {
						y = child.getY() + child.getHeight();
						g2d.drawLine(x, height - insets.bottom, x, y);
						Polygon polygon = new Polygon();
						polygon.addPoint(x - BOX, height - insets.bottom);
						polygon.addPoint(x + BOX - 1, height - insets.bottom);
						polygon.addPoint(x, height - BOX - insets.bottom);
						g2d.fillPolygon(polygon);
					} else if (vertical instanceof Bilateral) {
						y = ((Bilateral) vertical).getLeading();
						g2d.drawLine(x, insets.top, x, y + insets.top);
						Polygon polygon = new Polygon();
						polygon.addPoint(x - BOX + 1, insets.top);
						polygon.addPoint(x + BOX, insets.top);
						polygon.addPoint(x, BOX + insets.top);
						g2d.fillPolygon(polygon);
						y = child.getY() + child.getHeight();
						g2d.drawLine(x, height - insets.bottom, x, y);
						polygon = new Polygon();
						polygon.addPoint(x - BOX, height - insets.bottom);
						polygon.addPoint(x + BOX - 1, height - insets.bottom);
						polygon.addPoint(x, height - BOX - insets.bottom);
						g2d.fillPolygon(polygon);
					}
				}
			}
			g2d.setStroke(old);
			WidgetAdapter containerAdapter = WidgetAdapter.getWidgetAdapter(container);
			if (!containerAdapter.isSelected()) {
				int w = container.getWidth();
				int h = container.getHeight();
				g.setColor(Color.lightGray);
				g.drawRect(0, 0, w - 1, h - 1);
			}
		}
	}

	private List<IDragOperation> getCurrentOperation() {
		List<IDragOperation> dragOperations = null;
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int state = parent.getState();
		switch (state) {
		case Azimuth.STATE_BEAN_HOVER:
			dragOperations = beanHover;
			break;
		case Azimuth.STATE_BEAN_RESIZE_LEFT:
			dragOperations = resizeLeft;
			break;
		case Azimuth.STATE_BEAN_RESIZE_LEFT_TOP:
			dragOperations = resizeLeftTop;
			break;
		case Azimuth.STATE_BEAN_RESIZE_TOP:
			dragOperations = resizeTop;
			break;
		case Azimuth.STATE_BEAN_RESIZE_RIGHT_TOP:
			dragOperations = resizeRightTop;
			break;
		case Azimuth.STATE_BEAN_RESIZE_RIGHT:
			dragOperations = resizeRight;
			break;
		case Azimuth.STATE_BEAN_RESIZE_RIGHT_BOTTOM:
			dragOperations = resizeRightBottom;
			break;
		case Azimuth.STATE_BEAN_RESIZE_BOTTOM:
			dragOperations = resizeBottom;
			break;
		case Azimuth.STATE_BEAN_RESIZE_LEFT_BOTTOM:
			dragOperations = resizeLeftBottom;
			break;
		}
		return dragOperations;
	}

	@Override
	public boolean dragEnter(Point p) {
		initDragOperation(container);
		horizontal_baseline = null;
		vertical_baseline = null;
		hovered = true;
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int state = parent.getState();
		if (state == Azimuth.STATE_BEAN_HOVER)
			parent.setMascotLocation(p);
		List<IDragOperation> dragOperations = getCurrentOperation();
		if (dragOperations != null) {
			for (IDragOperation operation : dragOperations) {
				operation.dragEnter(p);
			}
		}
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		horizontal_baseline = null;
		vertical_baseline = null;
		hovered = false;
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int state = parent.getState();
		if (state == Azimuth.STATE_BEAN_HOVER)
			parent.setMascotLocation(p);
		List<IDragOperation> dragOperations = getCurrentOperation();
		if (dragOperations != null) {
			for (IDragOperation operation : dragOperations) {
				operation.dragExit(p);
			}
		}
		return true;
	}

	@Override
	public boolean dragOver(Point p) {
		if (!hovered) {
			initDragOperation(container);
			hovered = true;
		}
		horizontal_baseline = null;
		vertical_baseline = null;
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int state = parent.getState();
		if (state == Azimuth.STATE_BEAN_HOVER)
			parent.setMascotLocation(p);
		List<IDragOperation> dragOperations = getCurrentOperation();
		if (dragOperations != null) {
			for (IDragOperation operation : dragOperations) {
				operation.dragOver(p);
			}
		}
		return true;
	}

	@Override
	public boolean drop(Point p) {
		horizontal_baseline = null;
		vertical_baseline = null;
		hovered = false;
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int state = parent.getState();
		if (state == Azimuth.STATE_BEAN_HOVER)
			parent.setMascotLocation(p);
		List<IDragOperation> dragOperations = getCurrentOperation();
		if (dragOperations != null) {
			for (IDragOperation operation : dragOperations) {
				operation.drop(p);
			}
		}
		return true;
	}

	@Override
	public boolean isChildResizable() {
		return true;
	}

	@Override
	public boolean removeChild(Component child) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		last_constraints = layout.getConstraints(child);
		assert last_constraints != null;
		container.remove(child);
		container.validate();
		return true;
	}

	@Override
	public void initConainerLayout(Container container, IProgressMonitor monitor) {
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
		JavaUtil.setupLayoutLib(monitor);
	}

	@Override
	public boolean cloneLayout(JComponent panel) {
		panel.setLayout(copyLayout(panel));
		GroupLayout layout = (GroupLayout) container.getLayout();
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int count = parent.getChildCount();
		for (int i = 0; i < count; i++) {
			JComponent child = (JComponent) parent.getChild(i);
			WidgetAdapter cAdapter = WidgetAdapter.getWidgetAdapter(child);
			Constraints childConstraints = layout.getConstraints(child);
			panel.add(cAdapter.cloneWidget(), childConstraints==null?null:childConstraints.clone());
		}
		return true;
	}

	@Override
	protected LayoutManager copyLayout(Container con) {
		LayoutManager layout = con.getLayout();
		if (layout == null || layout.getClass() != GroupLayout.class)
			return new GroupLayout();
		else
			return layout;
	}

	@Override
	public void adjustLayout(Component widget) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		Constraints constraints = layout.getConstraints(widget);
		Alignment horizontal = constraints.getHorizontal();
		if (horizontal instanceof Leading) {
			Leading leading = (Leading) horizontal;
			if (leading.getSize() == Alignment.PREFERRED) {
				int now_width = widget.getWidth();
				int pref_width = widget.getPreferredSize().width;
				int delta_width = pref_width - now_width;
				if (delta_width != 0) {
					adjustHorizontalLeadingBy(widget, delta_width);
					container.doLayout();
					container.validate();
				}
			} else {
				int delta_width = leading.getSize() - widget.getWidth();
				if (delta_width != 0) {
					adjustHorizontalLeadingBy(widget, delta_width);
					container.doLayout();
					container.validate();
				}
			}
		} else if (horizontal instanceof Trailing) {
			Trailing trailing = (Trailing) horizontal;
			if (trailing.getSize() == Alignment.PREFERRED) {
				int now_width = widget.getWidth();
				int pref_width = widget.getPreferredSize().width;
				int delta_width = pref_width - now_width;
				if (delta_width != 0) {
					adjustHorizontalTrailingBy(widget, delta_width);
					container.doLayout();
					container.validate();
				}
			} else {
				int delta_width = trailing.getSize() - widget.getWidth();
				if (delta_width != 0) {
					adjustHorizontalTrailingBy(widget, delta_width);
					container.doLayout();
					container.validate();
				}
			}
		}

		Alignment vertical = constraints.getVertical();
		if (vertical instanceof Leading) {
			Leading leading = (Leading) vertical;
			if (leading.getSize() == Alignment.PREFERRED) {
				int now_height = widget.getHeight();
				int pref_height = widget.getPreferredSize().height;
				int delta_height = pref_height - now_height;
				if (delta_height != 0) {
					adjustVerticalLeadingBy(widget, delta_height);
					container.doLayout();
					container.validate();
				}
			} else {
				int delta_height = leading.getSize() - widget.getHeight();
				if (delta_height != 0) {
					adjustVerticalLeadingBy(widget, delta_height);
					container.doLayout();
					container.validate();
				}
			}
		} else if (vertical instanceof Trailing) {
			Trailing trailing = (Trailing) vertical;
			if (trailing.getSize() == Alignment.PREFERRED) {
				int now_height = widget.getHeight();
				int pref_height = widget.getPreferredSize().height;
				int delta_height = pref_height - now_height;
				if (delta_height != 0) {
					adjustVerticalTrailingBy(widget, delta_height);
					container.doLayout();
					container.validate();
				}
			} else {
				int delta_height = trailing.getSize() - widget.getHeight();
				if (delta_height != 0) {
					adjustVerticalTrailingBy(widget, delta_height);
					container.doLayout();
					container.validate();
				}
			}
		}
	}

	private void adjustVerticalTrailingBy(Component widget, int delta_height) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int count = containerAdapter.getChildCount();
		for (int i = 0; i < count; i++) {
			Component target = containerAdapter.getChild(i);
			if (target != widget) {
				if (isTopRelatedTo((JComponent) target, (JComponent) widget)) {
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

	private void adjustVerticalLeadingBy(Component widget, int delta_height) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int count = containerAdapter.getChildCount();
		for (int i = 0; i < count; i++) {
			Component target = containerAdapter.getChild(i);
			if (target != widget) {
				if (isBottomRelatedTo((JComponent) target, (JComponent) widget)) {
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

	private void adjustHorizontalTrailingBy(Component widget, int delta_width) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int count = containerAdapter.getChildCount();
		for (int i = 0; i < count; i++) {
			Component target = containerAdapter.getChild(i);
			if (target != widget) {
				if (isLeftRelatedTo((JComponent) target, (JComponent) widget)) {
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

	private void adjustHorizontalLeadingBy(Component widget, int delta_width) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int count = containerAdapter.getChildCount();
		for (int i = 0; i < count; i++) {
			Component target = containerAdapter.getChild(i);
			if (target != widget) {
				if (isRightRelatedTo((JComponent) target, (JComponent) widget)) {
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
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		return containerAdapter.getChildCount() > 1;
	}

	public boolean doAlignment(String id) {
		IUndoableOperation operation = null;
		if (id.equals("same_height")) { //$NON-NLS-1$
			operation = getSameHeight();
		} else if (id.equals("same_width")) { //$NON-NLS-1$
			operation = getSameWidth();
		} else if (id.equals("middle")) { //$NON-NLS-1$
			operation = getMiddle();
		} else if (id.equals("center")) { //$NON-NLS-1$
			operation = getCenter();
		} else if (id.equals("bottom")) { //$NON-NLS-1$
			operation = getBottom();
		} else if (id.equals("left")) { //$NON-NLS-1$
			operation = getLeft();
		} else if (id.equals("right")) { //$NON-NLS-1$
			operation = getRight();
		} else if (id.equals("top")) { //$NON-NLS-1$
			operation = getTop();
		}
		if (operation != null) {
			CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
			operation.addContext(parent.getUndoContext());
			IOperationHistory operationHist = PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
			try {
				operationHist.execute(operation, null, null);
				return true;
			} catch (ExecutionException e) {
				GroupLayoutPlugin.getLogger().error(e);
				return false;
			}
		}
		return false;
	}

	private IUndoableOperation getTop() {
		return new TopAlignmentOperation(container, this);
	}

	private IUndoableOperation getRight() {
		return new RightAlignmentOperation(container, this);
	}

	private IUndoableOperation getLeft() {
		return new LeftAlignmentOperation(container, this);
	}

	private IUndoableOperation getBottom() {
		return new BottomAlignmentOperation(container, this);
	}

	private IUndoableOperation getCenter() {
		return new CenterAlignmentOperation(container, this);
	}

	private IUndoableOperation getMiddle() {
		return new MiddleAlignmentOperation(container, this);
	}

	private IUndoableOperation getSameWidth() {
		return new SameWidthOperation(container, this);
	}

	private IUndoableOperation getSameHeight() {
		return new SameHeightOperation(container, this);
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		container.add(child, (Constraints) constraints);
	}

	@Override
	public Object getChildConstraints(Component child) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		Constraints constraints = layout.getConstraints(child);
		return constraints;
	}
}
