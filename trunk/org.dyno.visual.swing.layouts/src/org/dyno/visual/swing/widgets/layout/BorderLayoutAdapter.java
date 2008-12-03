
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

package org.dyno.visual.swing.widgets.layout;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.dyno.visual.swing.base.WidgetProperty;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.ILayoutBean;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.layout.actions.BorderLayoutPlacementAction;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.MenuManager;

public class BorderLayoutAdapter extends LayoutAdapter implements ILayoutBean {
	private List<Boolean> forbid;
	private List<Rectangle> placement;
	private List<String> constraints;

	@Override
	public void initConainerLayout(Container panel, IProgressMonitor monitor) {
		Container container = panel;
		CompositeAdapter compositeAdapter = (CompositeAdapter) WidgetAdapter
				.getWidgetAdapter(container);
		int count = compositeAdapter.getChildCount();
		ArrayList<Component> arrayList = new ArrayList<Component>();
		for (int i = 0; i < count; i++) {
			arrayList.add(compositeAdapter.getChild(i));
		}
		compositeAdapter.removeAllChild();
		panel.setLayout(new BorderLayout());
		for (int i = 0; i < count && i < 5; i++) {
			Component comp = arrayList.get(i);
			String constraintsObject = BorderLayout.CENTER;
			if (i == 0) {
				constraintsObject = BorderLayout.CENTER;
			} else if (i == 1) {
				constraintsObject = BorderLayout.NORTH;
			} else if (i == 2) {
				constraintsObject = BorderLayout.EAST;
			} else if (i == 3) {
				constraintsObject = BorderLayout.WEST;
			} else if (i == 4) {
				constraintsObject = BorderLayout.SOUTH;
			}
			container.add(comp, constraintsObject);
		}
	}

	@Override
	public boolean dragOver(Point p) {
		return drag(p);
	}

	@Override
	public void fillConstraintsAction(MenuManager menu, Component child) {
		MenuManager plcMenu = new MenuManager("Component Placement",
				"#BORDERLAYOUT_CONSTRAINTS");
		plcMenu.add(new BorderLayoutPlacementAction(container,
				BorderLayout.CENTER, (JComponent)child));
		plcMenu.add(new BorderLayoutPlacementAction(container,
				BorderLayout.NORTH, (JComponent)child));
		plcMenu.add(new BorderLayoutPlacementAction(container,
				BorderLayout.EAST, (JComponent)child));
		plcMenu.add(new BorderLayoutPlacementAction(container,
				BorderLayout.WEST, (JComponent)child));
		plcMenu.add(new BorderLayoutPlacementAction(container,
				BorderLayout.SOUTH, (JComponent)child));
		menu.add(plcMenu);
	}

	@Override
	public void paintFocused(Graphics g) {
		if (placement != null && forbid != null) {
			for (int i = 0; i < placement.size(); i++) {
				Rectangle place = placement.get(i);
				boolean fb = forbid.get(i).booleanValue();
				if (place != null) {
					Graphics2D g2d = (Graphics2D) g;
					Composite oldcomp=g2d.getComposite();
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					g2d.setColor(fb?RED_COLOR:GREEN_COLOR);
					g2d.fillRect(place.x, place.y, place.width, place.height);
					Stroke oldStroke = g2d.getStroke();
					g2d.setColor(fb ? GREEN_COLOR : RED_COLOR);
					g2d.setStroke(STROKE);
					g2d.drawRect(place.x, place.y, place.width, place.height);
					g2d.setStroke(oldStroke);
					g2d.setComposite(oldcomp);

				}
			}
		}
	}

	@Override
	public boolean dragEnter(Point p) {
		return drag(p);
	}

	private boolean drag(Point p) {
		int width = container.getWidth() - 1;
		int height = container.getHeight() - 1;
		Insets insets = container.getInsets();
		WidgetAdapter parent = WidgetAdapter.getWidgetAdapter(container);
		forbid = new ArrayList<Boolean>();
		placement = new ArrayList<Rectangle>();
		constraints = new ArrayList<String>();
		Point hsp = parent.getMascotLocation();
		for (WidgetAdapter todrop : parent.getDropWidget()) {
			Dimension pref = todrop.getWidget().getPreferredSize();
			int prefw = pref.width == 0 ? todrop.getWidget().getWidth()
					: pref.width;
			int prefh = pref.height == 0 ? todrop.getWidget().getHeight()
					: pref.height;
			BorderLayout layout = (BorderLayout) container.getLayout();
			Component nComp = layout.getLayoutComponent(BorderLayout.NORTH);
			int north = prefh + insets.top;
			if (nComp != null)
				north = nComp.getHeight() + insets.top;
			Component sComp = layout.getLayoutComponent(BorderLayout.SOUTH);
			int south = prefh + insets.bottom;
			if (sComp != null)
				south = sComp.getHeight() + insets.bottom;
			Component eComp = layout.getLayoutComponent(BorderLayout.EAST);
			int east = prefw + insets.right;
			if (eComp != null)
				east = eComp.getWidth() + insets.right;
			Component wComp = layout.getLayoutComponent(BorderLayout.WEST);
			int west = prefw + insets.left;
			if (wComp != null)
				west = wComp.getWidth() + insets.left;
			Component cComp = layout.getLayoutComponent(BorderLayout.CENTER);
			Point thsp=todrop.getHotspotPoint();
			int x = hsp.x-thsp.x+todrop.getWidget().getWidth()/2;
			int y = hsp.y-thsp.y+todrop.getWidget().getHeight()/2;
			if (y < north) {
				constraints.add(BorderLayout.NORTH);
				forbid.add(nComp != null);
				placement.add(new Rectangle(insets.left, insets.top, width
						- insets.left - insets.right, north - insets.top));
			} else if ((y >= north) && (y < (height - south))) {
				if (x < west) {
					constraints.add(BorderLayout.WEST);
					forbid.add(wComp != null);
					int t = nComp != null ? north : insets.top;
					int l = insets.left;
					int w = west - insets.left;
					int h = height - (nComp != null ? north : insets.top)
							- (sComp != null ? south : insets.bottom);
					placement.add(new Rectangle(l, t, w, h));
				} else if ((x >= west) && (x < (width - east))) {
					constraints.add(BorderLayout.CENTER);
					forbid.add(cComp != null);
					int t = nComp != null ? north : insets.top;
					int l = wComp != null ? west : insets.left;
					int w = width - (wComp != null ? west : insets.left)
							- (eComp != null ? east : insets.right);
					int h = height - (nComp != null ? north : insets.top)
							- (sComp != null ? south : insets.bottom);
					placement.add(new Rectangle(l, t, w, h));
				} else {
					constraints.add(BorderLayout.EAST);
					forbid.add(eComp != null);
					int t = nComp != null ? north : insets.top;
					int l = width - east;
					int w = east - insets.right;
					int h = height - (nComp != null ? north : insets.top)
							- (sComp != null ? south : insets.bottom);
					placement.add(new Rectangle(l, t, w, h));
				}
			} else {
				constraints.add(BorderLayout.SOUTH);
				forbid.add(sComp != null);
				placement.add(new Rectangle(insets.left, height - south, width
						- insets.left - insets.right, south - insets.bottom));
			}
		}
		parent.setMascotLocation(p);
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		placement = null;
		constraints = null;
		forbid = null;
		return true;
	}

	@Override
	public boolean drop(Point p) {
		drag(p);
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter
				.getWidgetAdapter(container);
		parent.clearAllSelected();
		if (forbid != null) {
			for (int i = 0; i < forbid.size(); i++) {
				boolean fb = forbid.get(i).booleanValue();
				if (fb) {
					Toolkit.getDefaultToolkit().beep();
				} else {
					WidgetAdapter todrop = parent.getDropWidget().get(i);
					if (constraints == null || constraints.get(i) == null){
						System.out.println("here");
						container.add(todrop.getComponent());
					}else {
						if (!constraints.get(i).equals(BorderLayout.CENTER)) {
							Component drop = todrop.getComponent();
							Dimension pref = drop.getPreferredSize();
							if (pref.width == 0 || pref.height == 0)
								drop.setPreferredSize(drop.getSize());
						}
						container
								.add(todrop.getComponent(), constraints.get(i));
					}
					todrop.setSelected(true);
				}
			}
		}
		parent.getRootAdapter().getWidget().validate();
		placement = null;
		constraints = null;
		forbid = null;
		return true;
	}

	@Override
	public boolean cloneLayout(JComponent panel) {
		panel.setLayout(copyLayout(panel));
		BorderLayout layout = (BorderLayout) container.getLayout();
		Component nComp = layout.getLayoutComponent(BorderLayout.NORTH);
		if (nComp != null) {
			WidgetAdapter nAdapter = WidgetAdapter
					.getWidgetAdapter((JComponent) nComp);
			panel.add(nAdapter.cloneWidget(), BorderLayout.NORTH);
		}
		Component sComp = layout.getLayoutComponent(BorderLayout.SOUTH);
		if (sComp != null) {
			WidgetAdapter sAdapter = WidgetAdapter
					.getWidgetAdapter((JComponent) sComp);
			panel.add(sAdapter.cloneWidget(), BorderLayout.SOUTH);
		}
		Component eComp = layout.getLayoutComponent(BorderLayout.EAST);
		if (eComp != null) {
			WidgetAdapter eAdapter = WidgetAdapter
					.getWidgetAdapter((JComponent) eComp);
			panel.add(eAdapter.cloneWidget(), BorderLayout.EAST);
		}
		Component wComp = layout.getLayoutComponent(BorderLayout.WEST);
		if (wComp != null) {
			WidgetAdapter wAdapter = WidgetAdapter
					.getWidgetAdapter((JComponent) wComp);
			panel.add(wAdapter.cloneWidget(), BorderLayout.WEST);
		}
		Component cComp = layout.getLayoutComponent(BorderLayout.CENTER);
		if (cComp != null) {
			WidgetAdapter cAdapter = WidgetAdapter
					.getWidgetAdapter((JComponent) cComp);
			panel.add(cAdapter.cloneWidget(), BorderLayout.CENTER);
		}
		return true;
	}

	@Override
	protected LayoutManager copyLayout(Container con) {
		BorderLayout layout = (BorderLayout) container.getLayout();
		BorderLayout copy = new BorderLayout();
		copy.setHgap(layout.getHgap());
		copy.setVgap(layout.getVgap());
		return copy;
	}

	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		BorderLayout layout = (BorderLayout) container.getLayout();
		int hgap = layout.getHgap();
		int vgap = layout.getVgap();
		if (hgap != 0 || vgap != 0) {
			return "new " + imports.addImport("java.awt.BorderLayout") + "("
					+ hgap + ", " + vgap + ")";
		} else {
			return "new " + imports.addImport("java.awt.BorderLayout") + "()";
		}
	}

	@Override
	public void addAfter(Component hovering, Component dragged) {
		addChild(dragged);
	}

	@Override
	public void addBefore(Component hovering, Component dragged) {
		addChild(dragged);
	}

	@Override
	public void addChild(Component widget) {
		String emptyCons = findEmptyConstraints();
		container.add(widget, emptyCons);
	}

	private String findEmptyConstraints() {
		BorderLayout layout = (BorderLayout) container.getLayout();
		if (layout.getLayoutComponent(BorderLayout.CENTER) == null)
			return BorderLayout.CENTER;
		if (layout.getLayoutComponent(BorderLayout.NORTH) == null)
			return BorderLayout.NORTH;
		if (layout.getLayoutComponent(BorderLayout.SOUTH) == null)
			return BorderLayout.SOUTH;
		if (layout.getLayoutComponent(BorderLayout.EAST) == null)
			return BorderLayout.EAST;
		if (layout.getLayoutComponent(BorderLayout.WEST) == null)
			return BorderLayout.WEST;
		return null;
	}

	@Override
	protected String getChildConstraints(Component child, ImportRewrite imports) {
		BorderLayout layout = (BorderLayout) container.getLayout();
		String object = (String) layout.getConstraints(child);
		if (object == null)
			return imports.addImport("java.awt.BorderLayout") + ".CENTER";
		else if (object.equals(BorderLayout.CENTER))
			return imports.addImport("java.awt.BorderLayout") + ".CENTER";
		else if (object.equals(BorderLayout.NORTH))
			return imports.addImport("java.awt.BorderLayout") + ".NORTH";
		else if (object.equals(BorderLayout.SOUTH))
			return imports.addImport("java.awt.BorderLayout") + ".SOUTH";
		else if (object.equals(BorderLayout.EAST))
			return imports.addImport("java.awt.BorderLayout") + ".EAST";
		else if (object.equals(BorderLayout.WEST))
			return imports.addImport("java.awt.BorderLayout") + ".WEST";
		return null;
	}

	@Override
	protected IWidgetPropertyDescriptor[] getLayoutProperties() {
		WidgetProperty hgapProperty = new WidgetProperty("hgap", "hgap",
				BorderLayout.class);
		WidgetProperty vgapProperty = new WidgetProperty("vgap", "vgap",
				BorderLayout.class);
		return new IWidgetPropertyDescriptor[] { hgapProperty, vgapProperty };
	}

	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		container.add(child, constraints);
	}

	@Override
	public Object getChildConstraints(Component child) {
		BorderLayout layout = (BorderLayout) container.getLayout();
		String object = (String) layout.getConstraints(child);
		if (object == null)
			return BorderLayout.CENTER;
		return object;
	}
}

