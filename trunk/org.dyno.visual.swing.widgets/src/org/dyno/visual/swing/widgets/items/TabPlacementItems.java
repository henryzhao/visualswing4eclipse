/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.items;

import javax.swing.JTabbedPane;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class TabPlacementItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("TOP", JTabbedPane.TOP, "javax.swing.JTabbedPane.TOP"),
			new Item("LEFT", JTabbedPane.LEFT, "javax.swing.JTabbedPane.LEFT"), new Item("BOTTOM", JTabbedPane.BOTTOM, "javax.swing.JTabbedPane.BOTTOM"),
			new Item("RIGHT", JTabbedPane.RIGHT, "javax.swing.JTabbedPane.RIGHT") };

	public TabPlacementItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}