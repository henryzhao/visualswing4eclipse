/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.layout;

import java.awt.BorderLayout;
import java.awt.Component;
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

import javax.swing.JComponent;

import org.dyno.visual.swing.base.WidgetProperty;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.ILayoutBean;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class BorderLayoutAdapter extends LayoutAdapter implements ILayoutBean {
	private boolean forbid;
	private Rectangle placement;
	private String constraints;

	@Override
	public void initConainerLayout(Container panel) {
		JComponent container = (JComponent) panel;
		CompositeAdapter compositeAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		int count = compositeAdapter.getChildCount();
		ArrayList<JComponent> arrayList = new ArrayList<JComponent>();
		for (int i = 0; i < count; i++) {
			arrayList.add(compositeAdapter.getChild(i));
		}
		compositeAdapter.removeAllChild();
		panel.setLayout(new BorderLayout());
		for (int i = 0; i < count && i < 5; i++) {
			JComponent comp = arrayList.get(i);
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
	public void paintFocused(Graphics g) {
		if (placement != null) {
			Graphics2D g2d = (Graphics2D) g;
			Stroke oldStroke = g2d.getStroke();
			g2d.setColor(forbid ? GREEN_COLOR : RED_COLOR);
			g2d.setStroke(STROKE);
			g2d.drawRect(placement.x, placement.y, placement.width, placement.height);
			g2d.setStroke(oldStroke);
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
		WidgetAdapter todrop = parent.getDropWidget();
		Dimension pref = todrop.getWidget().getPreferredSize();
		int prefw = pref.width == 0 ? todrop.getWidget().getWidth() : pref.width;
		int prefh = pref.height == 0 ? todrop.getWidget().getHeight() : pref.height;
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
		int x = p.x;
		int y = p.y;
		if (y < north) {
			constraints = BorderLayout.NORTH;
			forbid = (nComp != null);
			placement = new Rectangle(insets.left, insets.top, width - insets.left - insets.right, north - insets.top);
		} else if ((y >= north) && (y < (height - south))) {
			if (x < west) {
				constraints = BorderLayout.WEST;
				forbid = (wComp != null);
				int t = nComp != null ? north : insets.top;
				int l = insets.left;
				int w = west - insets.left;
				int h = height - (nComp != null ? north : insets.top) - (sComp != null ? south : insets.bottom);
				placement = new Rectangle(l, t, w, h);
			} else if ((x >= west) && (x < (width - east))) {
				constraints = BorderLayout.CENTER;
				forbid = (cComp != null);
				int t = nComp != null ? north : insets.top;
				int l = wComp != null ? west : insets.left;
				int w = width - (wComp != null ? west : insets.left) - (eComp != null ? east : insets.right);
				int h = height - (nComp != null ? north : insets.top) - (sComp != null ? south : insets.bottom);
				placement = new Rectangle(l, t, w, h);
			} else {
				constraints = BorderLayout.EAST;
				forbid = (eComp != null);
				int t = nComp != null ? north : insets.top;
				int l = width - east;
				int w = east - insets.right;
				int h = height - (nComp != null ? north : insets.top) - (sComp != null ? south : insets.bottom);
				placement = new Rectangle(l, t, w, h);
			}
		} else {
			constraints = BorderLayout.SOUTH;
			forbid = (sComp != null);
			placement = new Rectangle(insets.left, height - south, width - insets.left - insets.right, south - insets.bottom);
		}
		parent.setMascotLocation(p);
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		placement = null;
		constraints = null;
		forbid = false;
		return true;
	}

	@Override
	public boolean drop(Point p) {
		drag(p);
		if (forbid) {
			placement = null;
			constraints = null;
			forbid = false;
			Toolkit.getDefaultToolkit().beep();
			return true;
		} else {
			CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
			WidgetAdapter todrop = parent.getDropWidget();
			if (constraints == null)
				container.add(todrop.getComponent());
			else {
				if (!constraints.equals(BorderLayout.CENTER)) {
					JComponent drop = todrop.getComponent();
					Dimension pref = drop.getPreferredSize();
					if (pref.width == 0 || pref.height == 0)
						drop.setPreferredSize(drop.getSize());
				}
				container.add(todrop.getComponent(), constraints);
			}
			parent.getRootAdapter().getWidget().validate();
			parent.clearAllSelected();
			todrop.setSelected(true);
			placement = null;
			constraints = null;
			forbid = false;
			return true;
		}
	}

	@Override
	public boolean canAcceptMoreComponent() {
		BorderLayout layout = (BorderLayout) container.getLayout();
		Component nComp = layout.getLayoutComponent(BorderLayout.NORTH);
		if (nComp == null)
			return true;
		Component sComp = layout.getLayoutComponent(BorderLayout.SOUTH);
		if (sComp == null)
			return true;
		Component eComp = layout.getLayoutComponent(BorderLayout.EAST);
		if (eComp == null)
			return true;
		Component wComp = layout.getLayoutComponent(BorderLayout.WEST);
		if (wComp == null)
			return true;
		Component cComp = layout.getLayoutComponent(BorderLayout.CENTER);
		if (cComp == null)
			return true;
		return false;
	}

	@Override
	public boolean cloneLayout(JComponent panel) {
		panel.setLayout(copyLayout(panel));
		BorderLayout layout = (BorderLayout) container.getLayout();
		Component nComp = layout.getLayoutComponent(BorderLayout.NORTH);
		if (nComp != null) {
			WidgetAdapter nAdapter = WidgetAdapter.getWidgetAdapter((JComponent) nComp);
			panel.add(nAdapter.cloneWidget(), BorderLayout.NORTH);
		}
		Component sComp = layout.getLayoutComponent(BorderLayout.SOUTH);
		if (sComp != null) {
			WidgetAdapter sAdapter = WidgetAdapter.getWidgetAdapter((JComponent) sComp);
			panel.add(sAdapter.cloneWidget(), BorderLayout.SOUTH);
		}
		Component eComp = layout.getLayoutComponent(BorderLayout.EAST);
		if (eComp != null) {
			WidgetAdapter eAdapter = WidgetAdapter.getWidgetAdapter((JComponent) eComp);
			panel.add(eAdapter.cloneWidget(), BorderLayout.EAST);
		}
		Component wComp = layout.getLayoutComponent(BorderLayout.WEST);
		if (wComp != null) {
			WidgetAdapter wAdapter = WidgetAdapter.getWidgetAdapter((JComponent) wComp);
			panel.add(wAdapter.cloneWidget(), BorderLayout.WEST);
		}
		Component cComp = layout.getLayoutComponent(BorderLayout.CENTER);
		if (cComp != null) {
			WidgetAdapter cAdapter = WidgetAdapter.getWidgetAdapter((JComponent) cComp);
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
			return "new "+imports.addImport("java.awt.BorderLayout")+"(" + hgap + ", " + vgap + ")";
		} else {
			return "new "+imports.addImport("java.awt.BorderLayout")+"()";
		}
	}

	@Override
	protected String getChildConstraints(JComponent child, ImportRewrite imports) {
		BorderLayout layout = (BorderLayout) container.getLayout();
		String object = (String) layout.getConstraints(child);
		if (object == null)
			return imports.addImport("java.awt.BorderLayout")+".CENTER";
		else if (object.equals(BorderLayout.CENTER))
			return imports.addImport("java.awt.BorderLayout")+".CENTER";
		else if (object.equals(BorderLayout.NORTH))
			return imports.addImport("java.awt.BorderLayout")+".NORTH";
		else if (object.equals(BorderLayout.SOUTH))
			return imports.addImport("java.awt.BorderLayout")+".SOUTH";
		else if (object.equals(BorderLayout.EAST))
			return imports.addImport("java.awt.BorderLayout")+".EAST";
		else if (object.equals(BorderLayout.WEST))
			return imports.addImport("java.awt.BorderLayout")+".WEST";
		return null;
	}

	@Override
	protected IWidgetPropertyDescriptor[] getLayoutProperties() {
		WidgetProperty hgapProperty = new WidgetProperty("hgap","hgap", BorderLayout.class);
		WidgetProperty vgapProperty = new WidgetProperty("vgap", "vgap", BorderLayout.class);
		return new IWidgetPropertyDescriptor[]{hgapProperty, vgapProperty};
	}
}
