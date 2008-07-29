/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.designer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;

import org.dyno.visual.swing.WhiteBoard;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
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
		paintSelection(g, c);
		paintSelectionThumb(g, c);
		paintSelectionRegion(g, c);
		paintFocused(g, c);
		paintMascot(g, c);
		paintBaselineAnchor(g, c);
	}

	private void paintSelectionRegion(Graphics g, JComponent c) {
		GlassPlane plane = (GlassPlane) c;
		Rectangle region = plane.getSelectionRegion();
		if (region != null) {
			Color old = g.getColor();
			g.setColor(SELECTION_COLOR);
			g.drawRect(region.x, region.y, region.width, region.height);
			g.setColor(old);
		}
	}

	private void paintSelectionThumb(Graphics g, JComponent c) {
		paintTranverse(g, c, new ThumbTranverse(), WidgetAdapter.ADHERE_PAD);
	}

	class ThumbTranverse implements Tranverse {
		public void paint(Graphics g, JComponent jc) {
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
		JComponent root = designer.getRootWidget();
		if (root != null) {
			Rectangle rect = new Rectangle(0, 0, root.getWidth(), root
					.getHeight());
			rect = SwingUtilities.convertRectangle(root, rect, c);
			Graphics clipg = g.create(rect.x - ad, rect.y - ad, rect.width + 2
					* ad, rect.height + 2 * ad);
			tranverse(clipg, root, trans, ad);
			clipg.dispose();
		}
	}

	private void tranverse(Graphics g, JComponent jc, Tranverse trans, int ad) {
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
		void paint(Graphics g, JComponent jc);
	}

	private boolean isDesigningWidget(Component widget) {
		if (widget instanceof JComponent) {
			JComponent jcomp = (JComponent) widget;
			return WidgetAdapter.getWidgetAdapter(jcomp) != null;
		} else
			return false;
	}

	private void paintSelection(Graphics g, JComponent c) {
		paintTranverse(g, c, new SelectionTranverse(), 1);
	}

	class SelectionTranverse implements Tranverse {
		public void paint(Graphics g, JComponent jc) {
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

	private void paintFocused(Graphics g, JComponent c) {
		GlassPlane glassPlane = (GlassPlane) c;
		WidgetAdapter adapter = glassPlane.getHoveredAdapter();
		if (adapter != null) {
			paintFocusedAdapter(g, adapter);
		}
	}

	private void paintBaselineAnchor(Graphics g, JComponent c) {
		GlassPlane glassPlane = (GlassPlane) c;
		WidgetAdapter adapter = glassPlane.getHoveredAdapter();
		if (adapter != null) {
			paintBaselineAnchorAdapter(g, adapter);
		}
	}

	private void paintBaselineAnchorAdapter(Graphics g, WidgetAdapter focused) {
		JComponent jpar = focused.getWidget();
		Rectangle local = SwingUtilities.getLocalBounds(jpar);
		Rectangle pub = SwingUtilities.convertRectangle(jpar, local, designer);
		Graphics clipg = g.create(pub.x, pub.y, pub.width + 1, pub.height + 1);
		focused.paintBaselineAnchor(clipg);
		clipg.dispose();
	}

	private void paintFocusedAdapter(Graphics g, WidgetAdapter focused) {
		JComponent jpar = focused.getWidget();
		Rectangle local = SwingUtilities.getLocalBounds(jpar);
		Rectangle pub = SwingUtilities.convertRectangle(jpar, local, designer);
		Graphics clipg = g.create(pub.x, pub.y, pub.width + 1, pub.height + 1);
		focused.paintFocused(clipg);
		clipg.dispose();
	}

	private void paintMascot(Graphics g, JComponent c) {
		GlassPlane plane = (GlassPlane) c;
		Point e = plane.getHotspotPoint();
		if (e == null)
			return;
		WidgetAdapter adapter = WhiteBoard.getSelectedWidget();
		if (adapter == null)
			return;
		JComponent comp = adapter.getComponent();
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
