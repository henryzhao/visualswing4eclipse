/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.items;

import java.awt.Adjustable;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class ScrollBarOrientationItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("HORIZONTAL", Adjustable.HORIZONTAL, "java.awt.Adjustable.HORIZONTAL"),
			new Item("VERTICAL", Adjustable.VERTICAL, "java.awt.Adjustable.VERTICAL") };

	public ScrollBarOrientationItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}