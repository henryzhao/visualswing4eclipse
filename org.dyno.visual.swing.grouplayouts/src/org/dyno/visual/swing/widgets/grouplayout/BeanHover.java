
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

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Spring;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

class BeanHover extends AbstractDragOperation {
	public BeanHover(GroupLayoutAdapter adapter, WidgetAdapter tracingAdapter, GroupLayout layout,
			JComponent container) {
		super(adapter, tracingAdapter, layout, container);
	}
	public boolean dragOver(Point p) {
		if (last_point == null)
			return dragEnter(p);
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter
				.getWidgetAdapter(container);
		if (p.equals(last_point))
			return false;
		Point oldp = dragComponent(p);
		Point newp = parent.getMascotLocation();
		return oldp != null && !oldp.equals(newp);
	}

	
	public boolean dragEnter(Point p) {
		adapter.setHovered(true);
		CompositeAdapter parent = (CompositeAdapter) WidgetAdapter
				.getWidgetAdapter(container);
		parent.setMascotLocation(p);
		last_point = p;
		return true;
	}

	
	public boolean drop(Point p) {
		Insets insets = container.getInsets();
		WidgetAdapter dropAdapter = tracingAdapter;
		JComponent drop = (JComponent) dropAdapter.getParentContainer();
		QuartetPair pair = calMascotLocation(drop, p, azimuth);
		Point hot = dropAdapter.getHotspotPoint();
		Spring spring = new Spring(10, 10);// TODO should be replaced by a
		// container gap.
		if (pair == null) {
			Point np = new Point(p.x - hot.x, p.y - hot.y);
			Constraints constraints = new Constraints(new Leading(np.x
					- insets.left, drop.getWidth(), spring), new Leading(np.y
					- insets.top, drop.getHeight(), spring));
			container.add(drop, constraints);
		} else {
			Alignment horizontal = null;
			Alignment vertical = null;
			if (pair.hQuart == null && pair.vQuart != null) {
				int x = pair.vQuart.masc - hot.x;
				int y = p.y - hot.y;
				int width = drop.getWidth();
				int height = drop.getHeight();
				vertical = new Leading(y - insets.top, height, spring);
				horizontal = pair.vQuart.anchor.createHoveredAxis(drop,
						new Rectangle(x, y, width, height));
			} else if (pair.hQuart != null && pair.vQuart == null) {
				int x = p.x - hot.x;
				int y = pair.hQuart.masc - hot.y;
				int width = drop.getWidth();
				int height = drop.getHeight();
				horizontal = new Leading(x - insets.left, width, spring);
				vertical = pair.hQuart.anchor.createHoveredAxis(drop,
						new Rectangle(x, y, width, height));
			} else if (pair.hQuart != null && pair.vQuart != null) {
				int x = pair.vQuart.masc - hot.x;
				int y = pair.hQuart.masc - hot.y;
				int width = drop.getWidth();
				int height = drop.getHeight();
				vertical = pair.hQuart.anchor.createHoveredAxis(drop,
						new Rectangle(x, y, width, height));
				horizontal = pair.vQuart.anchor.createHoveredAxis(drop,
						new Rectangle(x, y, width, height));
			}
			assert vertical != null && horizontal != null;
			Constraints constraints = new Constraints(horizontal, vertical);
			container.add(drop, constraints);
		}
		last_point = null;
		return true;
	}

	
	public boolean dragExit(Point p) {
		return true;
	}
}

