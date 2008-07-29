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
public class HorizontalAlignmentItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("LEFT", SwingConstants.LEFT, "javax.swing.SwingConstants.LEFT"),
			new Item("CENTER", SwingConstants.CENTER, "javax.swing.SwingConstants.CENTER"),
			new Item("RIGHT", SwingConstants.RIGHT, "javax.swing.SwingConstants.RIGHT"),
			new Item("LEADING", SwingConstants.LEADING, "javax.swing.SwingConstants.LEADING"),
			new Item("TRAILING", SwingConstants.TRAILING, "javax.swing.SwingConstants.TRAILING") };

	public HorizontalAlignmentItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}
