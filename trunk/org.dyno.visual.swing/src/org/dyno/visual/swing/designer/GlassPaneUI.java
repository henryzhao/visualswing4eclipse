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

package org.dyno.visual.swing.designer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;

import org.dyno.visual.swing.WhiteBoard;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.MenuSelectionManager;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IContextCustomizer;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

/**
 * 
 * GlassPaneUI
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class GlassPaneUI extends ComponentUI {
	static {
		THUMB_NAIL = new ImageIcon(GlassPaneUI.class.getClassLoader()
				.getResource("icons/resize_thumb.png"));
	}
	private VisualDesigner designer;

	/** Creates a new instance of DesignerUI */
	public GlassPaneUI() {
	}

	public void installUI(JComponent c) {
		designer = ((GlassPlane) c).getDesigner();
	}

	/**
	 */
	public void paint(Graphics g, JComponent c) {
		paintGrid(g, c);
		paintSelection(g, c);
		paintSelectionThumb(g, c);
		paintSelectionRegion(g, c);
		paintHovered(g, c);
		paintContextCustomizer(g, c);
		paintMascot(g, c);
		paintAnchor(g, c);
		paintHint(g, c);
	}


	private void paintGrid(Graphics g, JComponent c) {
		GlassPlane glassPlane = (GlassPlane) c;
		CompositeAdapter focused = glassPlane.getFocusedContainer();
		if (focused != null) {
			paintAdapterGrid(g, focused);
		}		
	}
	private void paintAdapterGrid(Graphics g, CompositeAdapter focused) {
		Component jpar = focused.getWidget();
		if (focused.isRoot())
			jpar = focused.getRootPane();
		Rectangle local = SwingUtilities.getLocalBounds(jpar);
		Rectangle pub = SwingUtilities.convertRectangle(jpar, local, designer);
		Graphics clipg = g.create(pub.x, pub.y, pub.width + 1, pub.height + 1);
		focused.paintGrid(clipg);
		clipg.dispose();
	}
	private void paintAdapterAnchor(Graphics g, CompositeAdapter focused) {
		Component jpar = focused.getWidget();
		if (focused.isRoot())
			jpar = focused.getRootPane();
		Rectangle local = SwingUtilities.getLocalBounds(jpar);
		Rectangle pub = SwingUtilities.convertRectangle(jpar, local, designer);
		Graphics clipg = g.create(pub.x, pub.y, pub.width + 1, pub.height + 1);
		focused.paintAnchor(clipg);
		clipg.dispose();
	}	
	private void paintContextCustomizer(Graphics g, JComponent c) {
		Component root = designer.getRoot();
		if (root != null) {
			WidgetAdapter rootAdapter = WidgetAdapter.getWidgetAdapter(root);
			if (rootAdapter != null) {
				List<IContextCustomizer> contextCustomizers = ExtensionRegistry
						.getContextCustomizers();
				for (IContextCustomizer customizer : contextCustomizers) {
					customizer.paintContext(g, WidgetAdapter
							.getWidgetAdapter(designer.getRoot()));
				}
			}
		}
	}

	private void paintSelectionRegion(Graphics g, JComponent c) {
		GlassPlane plane = (GlassPlane) c;
		Rectangle region = plane.getSelectionRegion();
		if (region != null) {
			Color old = g.getColor();
			g.setColor(new Color(0, 164, 255));
			((Graphics2D)g).setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 5 }, 0));
			g.drawRect(region.x, region.y, region.width, region.height);
			g.setColor(old);
		}
	}

	private void paintSelectionThumb(Graphics g, JComponent c) {
		if (designer.getSelectedComponents().size() == 1)
			paintTranverse(g, c, new ThumbTranverse(), WidgetAdapter.ADHERE_PAD);
	}

	class ThumbTranverse implements Tranverse {
		public void paint(Graphics g, Component jc) {			
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(jc);
			if (adapter.isSelected() && adapter.isResizable()) {
				int w = jc.getWidth();
				int h = jc.getHeight();
				int pad = WidgetAdapter.ADHERE_PAD;
				THUMB_NAIL.paintIcon(jc, g, 0, 0);
				THUMB_NAIL.paintIcon(jc, g, (w + pad) / 2, 0);
				THUMB_NAIL.paintIcon(jc, g, w + pad, 0);
				THUMB_NAIL.paintIcon(jc, g, w + pad, (h + pad) / 2);
				THUMB_NAIL.paintIcon(jc, g, w + pad, h + pad);
				THUMB_NAIL.paintIcon(jc, g, (w + pad) / 2, h + pad);
				THUMB_NAIL.paintIcon(jc, g, 0, h + pad);
				THUMB_NAIL.paintIcon(jc, g, 0, (h + pad) / 2);
			}
		}
	}

	private static Icon THUMB_NAIL;

	private void paintTranverse(Graphics g, JComponent c, Tranverse trans,
			int ad) {
		Component root = designer.getRootWidget();
		if (root != null) {
			Rectangle rect = new Rectangle(0, 0, root.getWidth(), root
					.getHeight());
			rect = SwingUtilities.convertRectangle(root, rect, c);
			Graphics clipg = g.create(rect.x - ad, rect.y - ad, rect.width + 2
					* ad, rect.height + 2 * ad);
			tranverse(clipg, root, trans, ad);
			clipg.dispose();
			tranverseMenuElement(g, trans, ad);
		}
	}

	private void tranverseMenuElement(Graphics g, Tranverse trans, int ad) {
		Point vdl = designer.getLocationOnScreen();
		MenuElement[] menu_selection = MenuSelectionManager.defaultManager()
				.getSelectedPath();
		if (menu_selection != null && menu_selection.length > 0) {
			for (int i = menu_selection.length - 1; i >= 0; i--) {
				if (menu_selection[i] instanceof JPopupMenu) {
					JPopupMenu jpm = (JPopupMenu) menu_selection[i];
					if (!jpm.isShowing())
						continue;
					MenuElement[] sub = jpm.getSubElements();
					Rectangle b = jpm.getBounds();
					Point jpml = jpm.getLocationOnScreen();
					b.x = jpml.x - vdl.x;
					b.y = jpml.y - vdl.y;
					for (MenuElement submenu : sub) {
						if (submenu instanceof JMenuItem) {
							JMenuItem jmi = (JMenuItem) submenu;
							if (isDesigningWidget(jmi)) {
								Rectangle sb = jmi.getBounds();
								sb.x += b.x;
								sb.y += b.y;
								Graphics clipg = g.create(sb.x - ad, sb.y - ad,
										sb.width + 2 * ad, sb.height + 2 * ad);
								trans.paint(clipg, jmi);
								clipg.dispose();
							}
						}
					}
				}
			}
		}
	}

	private void tranverse(Graphics g, Component jc, Tranverse trans, int ad) {
		if (isDesigningWidget(jc))
			trans.paint(g, jc);
		WidgetAdapter widget = WidgetAdapter.getWidgetAdapter(jc);
		if (widget instanceof CompositeAdapter) {
			CompositeAdapter parent = (CompositeAdapter) widget;
			int size = parent.getChildCount();
			for (int i = 0; i < size; i++) {
				Component child = parent.getChild(i);
				Rectangle rect = new Rectangle(0, 0, child.getWidth(), child
						.getHeight());
				rect = SwingUtilities.convertRectangle(child, rect, jc);
				Graphics clipg = g.create(rect.x, rect.y, rect.width + 2 * ad,
						rect.height + 2 * ad);
				tranverse(clipg, (JComponent) child, trans, ad);
				clipg.dispose();
			}
		}
	}

	interface Tranverse {
		void paint(Graphics g, Component jc);
	}

	private boolean isDesigningWidget(Component widget) {
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(widget);
		return adapter != null && (adapter.isRoot()||adapter.getName() != null);
	}

	private void paintSelection(Graphics g, JComponent c) {
		paintTranverse(g, c, new SelectionTranverse(), 1);
	}

	class SelectionTranverse implements Tranverse {
		public void paint(Graphics g, Component jc) {
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(jc);
			if (adapter.isSelected()) {
				int w = jc.getWidth();
				int h = jc.getHeight();
				Color old = g.getColor();
				g.setColor(SELECTION_COLOR);
				g.drawRect(0, 0, w + 1, h + 1);
				g.setColor(old);
			}
		}
	}

	private static Color SELECTION_COLOR = new Color(255, 164, 0);

	private void paintHovered(Graphics g, JComponent c) {
		GlassPlane glassPlane = (GlassPlane) c;
		CompositeAdapter hovered = glassPlane.getHoveredAdapter();
		if (hovered != null) {
			paintHoveredAdapter(g, hovered);
		}
	}

	private void paintHint(Graphics g, JComponent c) {
		GlassPlane glassPlane = (GlassPlane) c;
		CompositeAdapter hovered = glassPlane.getHoveredAdapter();
		if (hovered != null) {
			paintHintAdapter(g, hovered);
		}
	}

	private void paintAnchor(Graphics g, JComponent c) {
		GlassPlane glassPlane = (GlassPlane) c;
		CompositeAdapter focused = glassPlane.getFocusedContainer();
		if (focused != null) {
			paintAdapterAnchor(g, focused);
		}		
	}


	private void paintHintAdapter(Graphics g, CompositeAdapter hovered) {
		Component jpar = hovered.getWidget();
		if (hovered.isRoot())
			jpar = hovered.getRootPane();
		Rectangle local = SwingUtilities.getLocalBounds(jpar);
		Rectangle pub = SwingUtilities.convertRectangle(jpar, local, designer);
		Graphics clipg = g.create(pub.x, pub.y, pub.width + 1, pub.height + 1);
		hovered.paintHint(clipg);
		clipg.dispose();
	}

	private void paintHoveredAdapter(Graphics g, CompositeAdapter hovered) {
		Component jpar = hovered.getWidget();
		if (hovered.isRoot())
			jpar = hovered.getRootPane();
		if (hovered.needGlobalGraphics()) {
			hovered.paintHovered(g);
		} else {
			Rectangle local = SwingUtilities.getLocalBounds(jpar);
			Rectangle pub = SwingUtilities.convertRectangle(jpar, local, designer);
			Graphics clipg = g.create(pub.x, pub.y, pub.width + 1, pub.height + 1);
			hovered.paintHovered(clipg);
			clipg.dispose();
		}
	}

	private void paintMascot(Graphics g, JComponent c) {
		GlassPlane plane = (GlassPlane) c;
		Point e = plane.getHotspotPoint();
		if (e == null)
			return;
		List<WidgetAdapter> adapters = WhiteBoard.getSelectedWidget();
		if (adapters == null)
			return;
		for (WidgetAdapter adapter : adapters) {
			Component comp = adapter.getComponent();
			int w = comp.getWidth();
			int h = comp.getHeight();
			Point hs = adapter.getHotspotPoint();
			int x = e.x - hs.x;
			int y = e.y - hs.y;
			Graphics clipg = g.create(x - 1, y - 1, w + 2, h + 2);
			adapter.paintMascot(clipg);
			clipg.dispose();
		}
	}
}

