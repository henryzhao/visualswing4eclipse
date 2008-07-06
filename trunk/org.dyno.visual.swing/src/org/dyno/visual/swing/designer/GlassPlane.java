/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.designer;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

/**
 * 
 * GlassPlane
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class GlassPlane extends JComponent implements MouseListener {
	private static final long serialVersionUID = -409591239915380500L;
	private VisualDesigner designer;
	private Point hotspotPoint;
	private Rectangle selectionRegion;
	private GlassTarget target;
	private CompositeAdapter focusedComposite;

	public void setFocusedComposite(CompositeAdapter adapter) {
		this.focusedComposite = adapter;
		repaint();
	}

	@Override
	public void requestFocus() {
	}

	public CompositeAdapter getFocusedComposite() {
		return focusedComposite;
	}

	boolean editComponent(JComponent hovered) {
		return target.editComponent(hovered);
	}

	public GlassPlane(final VisualDesigner designer) {
		setLayout(null);
		this.designer = designer;
		addMouseListener(this);
		target = new GlassTarget(this);
		addMouseListener(target);
		addMouseMotionListener(target);
		addMouseWheelListener(target);
		setDropTarget(target);
		setTransferHandler(new GlassPlaneHandler());
		setFocusable(true);
		updateUI();
	}

	public int getState() {
		return target.getState();
	}

	class GlassPlaneHandler extends TransferHandler {
		private static final long serialVersionUID = -8997179076129970604L;
	}

	public void setGestureCursor(int t) {
		Window w = SwingUtilities.getWindowAncestor(this);
		int type = w.getCursor().getType();
		if (type != t) {
			w.setCursor(Cursor.getPredefinedCursor(t));
		}
	}

	public Rectangle getSelectionRegion() {
		return this.selectionRegion;
	}

	public void setSelectionRegion(Rectangle region) {
		this.selectionRegion = region;
		repaint();
	}

	public VisualDesigner getDesigner() {
		return designer;
	}

	public void updateUI() {
		setUI(new GlassPaneUI());
	}

	public Point getHotspotPoint() {
		return hotspotPoint;
	}

	public void setHotspotPoint(Point e) {
		hotspotPoint = e;
	}

	WidgetAdapter getHoveredAdapter() {
		return target.getHoveredApdater();
	}

	boolean isWidgetEditing() {
		return target.isWidgetEditing();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		super.requestFocus();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			designer.trigPopup(e.getPoint());
		}
	}

	public void setFocus() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GlassPlane.super.requestFocus();
			}
		});
	}
}
