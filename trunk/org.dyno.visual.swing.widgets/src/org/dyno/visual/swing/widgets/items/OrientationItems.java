/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.items;

import javax.swing.SwingConstants;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class OrientationItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("HORIZONTAL", SwingConstants.HORIZONTAL, "javax.swing.SwingConstants.HORIZONTAL"),
			new Item("VERTICAL", SwingConstants.VERTICAL, "javax.swing.SwingConstants.VERTICAL") };

	public OrientationItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}