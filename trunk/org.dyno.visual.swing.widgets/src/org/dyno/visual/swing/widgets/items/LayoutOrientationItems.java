/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.items;

import javax.swing.JList;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class LayoutOrientationItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("VERTICAL", JList.VERTICAL, "javax.swing.JList.VERTICAL"),
			new Item("HORIZONTAL_WRAP", JList.HORIZONTAL_WRAP, "javax.swing.JList.HORIZONTAL_WRAP"),
			new Item("VERTICAL_WRAP", JList.VERTICAL_WRAP, "javax.swing.JList.VERTICAL_WRAP") };

	public LayoutOrientationItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}