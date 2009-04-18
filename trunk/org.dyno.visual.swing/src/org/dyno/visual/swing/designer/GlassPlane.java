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
import org.eclipse.albireo.core.Platform;

/**
 * 
 * GlassPlane
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
@SuppressWarnings("unchecked")
public class GlassPlane extends JComponent implements MouseListener {
	private static final long serialVersionUID = -409591239915380500L;
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
		this.hotspotPoint = new Point(0,0);
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
		if(hotspotPoint!=null&&e!=null&&e.distance(hotspotPoint)>10){
			System.out.println();
		}
		hotspotPoint = e;
	}

	CompositeAdapter getSelectedContainer() {
		return designer.getSelectedContainer();
	}

	CompositeAdapter getFocusedContainer() {
		return designer.getFocusedContainer();
	}
	
	CompositeAdapter getHoveredAdapter(){
		return target.getHoveredAdapter();
	}

	CompositeAdapter getHintedAdapter() {
		return target.getHoveredAdapter();
	}
	boolean isWidgetEditing() {
		return target.isWidgetEditing();
	}

	
	public void mouseClicked(MouseEvent e) {
	}

	
	public void mouseEntered(MouseEvent e) {
	}

	
	public void mouseExited(MouseEvent e) {
	}

	
	public void mousePressed(MouseEvent e) {
		if (!e.isConsumed()&&Platform.isGtk() && e.isPopupTrigger()) {
			designer.trigPopup(e.getPoint(), designer.getSelectedComponents());
		}
		setFocus();
	}

	
	public void mouseReleased(MouseEvent e) {
		if (!e.isConsumed()&&!Platform.isGtk()&&e.isPopupTrigger()) {
			designer.trigPopup(e.getPoint(), designer.getSelectedComponents());
		}
	}

	public void setFocus() {
		designer.getEditor().getDisplay().asyncExec(new Runnable(){
			public void run() {
				setSwingFocus();
			}});
	}
	private void setSwingFocus(){
		designer.getEditor().setFocus();
		SwingUtilities.invokeLater(new Runnable() {
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

