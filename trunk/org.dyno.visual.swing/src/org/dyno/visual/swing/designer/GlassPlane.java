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

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.lang.reflect.Proxy;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.swt_awt.Platform;

/**
 * 
 * GlassPlane
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class GlassPlane extends JComponent implements MouseListener {
	private static final long serialVersionUID = -409591239915380500L;
	@SuppressWarnings("unchecked")
	private static final Class[]MOUSE_INTERFACES={MouseListener.class, MouseMotionListener.class, MouseWheelListener.class};
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
	boolean stopEditing(){
		return target.stopEditing();
	}
	boolean editComponent(Component hovered) {
		return target.editComponent(hovered);
	}
	public GlassPlane(final VisualDesigner designer) {
		MouseDelegateHandler handler = new MouseDelegateHandler(designer);
		ClassLoader loader = getClass().getClassLoader();
		Object proxy=Proxy.newProxyInstance(loader, MOUSE_INTERFACES, handler);
		addMouseListener((MouseListener) proxy);
		addMouseMotionListener((MouseMotionListener) proxy);
		addMouseWheelListener((MouseWheelListener) proxy);
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

	CompositeAdapter getFocusedContainer() {
		return designer.getFocusedContainer();
	}
	CompositeAdapter getHoveredAdapter(){
		return target.getHoveredAdapter();
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
		if (!e.isConsumed()&&Platform.isGtk() && e.isPopupTrigger()) {
			designer.trigPopup(e.getPoint(), designer.getSelectedComponents());
		}
		super.requestFocus();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!e.isConsumed()&&!Platform.isGtk()&&e.isPopupTrigger()) {
			designer.trigPopup(e.getPoint(), designer.getSelectedComponents());
		}
		super.requestFocus();
	}

	public void setFocus() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GlassPlane.super.requestFocus();
			}
		});
	}

	public void setCursorType(int type) {
		if(getCursor().getType()!=type)
			setCursor(Cursor.getPredefinedCursor(type));
	}
}

