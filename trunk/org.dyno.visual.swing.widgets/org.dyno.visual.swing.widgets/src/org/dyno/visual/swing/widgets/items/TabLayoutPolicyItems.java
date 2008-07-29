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
public class TabLayoutPolicyItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("WRAP_TAB_LAYOUT", JTabbedPane.WRAP_TAB_LAYOUT, "javax.swing.JTabbedPane.WRAP_TAB_LAYOUT"),
			new Item("SCROLL_TAB_LAYOUT", JTabbedPane.SCROLL_TAB_LAYOUT, "javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT") };

	public TabLayoutPolicyItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}