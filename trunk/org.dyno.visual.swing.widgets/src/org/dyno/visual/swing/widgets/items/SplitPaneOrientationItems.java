/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.items;

import javax.swing.JSplitPane;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class SplitPaneOrientationItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("HORIZONTAL_SPLIT", JSplitPane.HORIZONTAL_SPLIT, "javax.swing.JSplitPane.HORIZONTAL_SPLIT"),
			new Item("VERTICAL_SPLIT", JSplitPane.VERTICAL_SPLIT, "javax.swing.JSplitPane.VERTICAL_SPLIT") };

	public SplitPaneOrientationItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}