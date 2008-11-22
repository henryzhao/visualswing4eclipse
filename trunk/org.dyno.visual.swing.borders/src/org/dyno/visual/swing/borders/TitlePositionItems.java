/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.borders;

import javax.swing.border.TitledBorder;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class TitlePositionItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("ABOVE_TOP", TitledBorder.ABOVE_TOP, "javax.swing.border.TitledBorder.ABOVE_TOP"),
			new Item("TOP", TitledBorder.TOP, "javax.swing.border.TitledBorder.TOP"),
			new Item("BELOW_TOP", TitledBorder.BELOW_TOP, "javax.swing.border.TitledBorder.BELOW_TOP"),
			new Item("ABOVE_BOTTOM", TitledBorder.ABOVE_BOTTOM, "javax.swing.border.TitledBorder.ABOVE_BOTTOM"),
			new Item("BOTTOM", TitledBorder.BOTTOM, "javax.swing.border.TitledBorder.BOTTOM"),
			new Item("BELOW_BOTTOM", TitledBorder.BELOW_BOTTOM, "javax.swing.border.TitledBorder.BELOW_BOTTOM"),
			new Item("DEFAULT_POSITION", TitledBorder.DEFAULT_POSITION, "javax.swing.border.TitledBorder.DEFAULT_POSITION") };

	public TitlePositionItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}