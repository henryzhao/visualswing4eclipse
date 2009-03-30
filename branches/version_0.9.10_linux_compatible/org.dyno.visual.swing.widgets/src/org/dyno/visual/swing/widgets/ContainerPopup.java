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

package org.dyno.visual.swing.widgets;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JApplet;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.Popup;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

public class ContainerPopup extends Popup {
    /**
     * The Component representing the Popup.
     */
    private Component component;
	
	/** Component we are to be added to. */
	Component owner;
	/** Desired x location. */
	int x;
	/** Desired y location. */
	int y;

	public void hide() {
		Component component = getPopupComponent();

		if (component != null) {
			Container parent = component.getParent();

			if (parent != null) {
				Rectangle bounds = component.getBounds();

				parent.remove(component);
				parent.repaint(bounds.x, bounds.y, bounds.width, bounds.height);
			}
		}
		owner = null;
	}

	void packPopup() {
		Component component = getPopupComponent();

		if (component != null) {
			component.setSize(component.getPreferredSize());
		}
	}

	/**
	 * Returns the <code>Component</code> returned from
	 * <code>createComponent</code> that will hold the <code>Popup</code>.
	 */
	Component getPopupComponent() {
		return component;
	}

	Component newComponent(Component owner) {
		if (GraphicsEnvironment.isHeadless()) {
			// Generally not useful, bail.
			return null;
		}
		return new HeavyWeightWindow(getParentWindow(owner));
	}
    private Window getParentWindow(Component owner) {
        Window window = null;

        if (owner instanceof Window) {
            window = (Window)owner;
        }
        else if (owner != null) {
            window = SwingUtilities.getWindowAncestor(owner);
        }
        if (window == null) {
            window = new DefaultFrame();
        }
        return window;
    }
    /**
     * Used if no valid Window ancestor of the supplied owner is found.
     * <p>
     * PopupFactory uses this as a way to know when the Popup shouldn't
     * be cached based on the Window.
     */
    static class DefaultFrame extends Frame {
		private static final long serialVersionUID = 1L;
    }    
	static class HeavyWeightWindow extends JWindow {
		private static final long serialVersionUID = 1L;
		HeavyWeightWindow(Window parent) {
			super(parent);
			setFocusableWindowState(false);
			setName("###overrideRedirect###");
			try {
				setAlwaysOnTop(true);
			} catch (SecurityException se) {
				// setAlwaysOnTop is restricted,
				// the exception is ignored
			}
		}

		public void update(Graphics g) {
			paint(g);
		}

		@SuppressWarnings("deprecation")
		public void show() {
			this.pack();
			super.show();
		}
	}

	void resetPopup(Component owner, Component contents, int ownerX, int ownerY) {
		if ((owner instanceof JFrame) || (owner instanceof JDialog) || (owner instanceof JWindow)) {
			// Force the content to be added to the layered pane, otherwise
			// we'll get an exception when adding to the RootPaneContainer.
			owner = ((RootPaneContainer) owner).getLayeredPane();
		}
		if (getPopupComponent() == null) {
			component = newComponent(owner);
		}

		Component c = getPopupComponent();

		if (c instanceof JWindow) {
			JWindow component = (JWindow) getPopupComponent();

			component.setLocation(ownerX, ownerY);
			component.getContentPane().add(contents, BorderLayout.CENTER);
			contents.invalidate();
			if (component.isVisible()) {
				// Do not call pack() if window is not visible to
				// avoid early native peer creation
				packPopup();
			}
		}

		x = ownerX;
		y = ownerY;
		this.owner = owner;
	}

	boolean overlappedByOwnedWindow() {
		Component component = getPopupComponent();
		if (owner != null && component != null) {
			Window w = SwingUtilities.getWindowAncestor(owner);
			if (w == null) {
				return false;
			}
			Window[] ownedWindows = w.getOwnedWindows();
			if (ownedWindows != null) {
				Rectangle bnd = component.getBounds();
				for (int i = 0; i < ownedWindows.length; i++) {
					Window owned = ownedWindows[i];
					if (owned.isVisible() && bnd.intersects(owned.getBounds())) {

						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if the Popup can fit on the screen.
	 */
	boolean fitsOnScreen() {
		Component component = getPopupComponent();

		if (owner != null && component != null) {
			Container parent;
			int width = component.getWidth();
			int height = component.getHeight();
			for (parent = owner.getParent(); parent != null; parent = parent.getParent()) {
				if (parent instanceof JFrame || parent instanceof JDialog || parent instanceof JWindow) {

					Rectangle r = parent.getBounds();
					Insets i = parent.getInsets();
					r.x += i.left;
					r.y += i.top;
					r.width -= (i.left + i.right);
					r.height -= (i.top + i.bottom);

					GraphicsConfiguration gc = parent.getGraphicsConfiguration();
					Rectangle popupArea = getContainerPopupArea(gc);
					return r.intersection(popupArea).contains(x, y, width, height);

				} else if (parent instanceof JApplet) {
					Rectangle r = parent.getBounds();
					Point p = parent.getLocationOnScreen();

					r.x = p.x;
					r.y = p.y;
					return r.contains(x, y, width, height);
				} else if (parent instanceof Window || parent instanceof Applet) {
					// No suitable swing component found
					break;
				}
			}
		}
		return false;
	}

	Rectangle getContainerPopupArea(GraphicsConfiguration gc) {
		Rectangle screenBounds;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Insets insets;
		if (gc != null) {
			// If we have GraphicsConfiguration use it
			// to get screen bounds
			screenBounds = gc.getBounds();
			insets = toolkit.getScreenInsets(gc);
		} else {
			// If we don't have GraphicsConfiguration use primary screen
			screenBounds = new Rectangle(toolkit.getScreenSize());
			insets = new Insets(0, 0, 0, 0);
		}
		// Take insets into account
		screenBounds.x += insets.left;
		screenBounds.y += insets.top;
		screenBounds.width -= (insets.left + insets.right);
		screenBounds.height -= (insets.top + insets.bottom);
		return screenBounds;
	}
}

