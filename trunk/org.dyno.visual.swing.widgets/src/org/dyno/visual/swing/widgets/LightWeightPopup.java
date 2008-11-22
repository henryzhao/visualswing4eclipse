/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.widgets;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Window;

import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

public class LightWeightPopup extends ContainerPopup {
	public void hide() {
		super.hide();

		Container component = (Container) getComponent();

		component.removeAll();
	}

	public void show() {
		Container parent = null;

		if (owner != null) {
			parent = (owner instanceof Container ? (Container) owner : owner.getParent());
		}

		// Try to find a JLayeredPane and Window to add
		for (Container p = parent; p != null; p = p.getParent()) {
			if (p instanceof JComponent && ((JComponent) p).getClientProperty("popup.layer") != null) {
				parent = (Container)((JComponent) p).getClientProperty("popup.layer");
				break;
			} else if (p instanceof JRootPane) {
				if (p.getParent() instanceof JInternalFrame) {
					continue;
				}
				parent = ((JRootPane) p).getLayeredPane();
			} else if (p instanceof Window) {
				if (parent == null) {
					parent = p;
				}
				break;
			} else if (p instanceof JApplet) {
				// Painting code stops at Applets, we don't want
				// to add to a Component above an Applet otherwise
				// you'll never see it painted.
				break;
			}
		}

		Point p = convertScreenLocationToParent(parent, x, y);
		Component component = getComponent();

		component.setLocation(p.x, p.y);
		if (parent instanceof JLayeredPane) {
			((JLayeredPane) parent).add(component, JLayeredPane.POPUP_LAYER, 0);
		} else {
			parent.add(component);
		}
	}

	static Point convertScreenLocationToParent(Container parent, int x, int y) {
		for (Container p = parent; p != null; p = p.getParent()) {
			if (p instanceof Window) {
				Point point = new Point(x, y);

				SwingUtilities.convertPointFromScreen(point, parent);
				return point;
			}
		}
		throw new Error("convertScreenLocationToParent: no window ancestor");
	}

	Component createComponent(Component owner) {
		JComponent component = new JPanel(new BorderLayout(), true);

		component.setOpaque(true);
		return component;
	}

	//
	// Local methods
	//

	/**
	 * Resets the <code>Popup</code> to an initial state.
	 */
	void reset(Component owner, Component contents, int ownerX, int ownerY) {
		super.reset(owner, contents, ownerX, ownerY);

		JComponent component = (JComponent) getComponent();

		component.setOpaque(contents.isOpaque());
		component.setLocation(ownerX, ownerY);
		component.add(contents, BorderLayout.CENTER);
		contents.invalidate();
		pack();
	}
}
