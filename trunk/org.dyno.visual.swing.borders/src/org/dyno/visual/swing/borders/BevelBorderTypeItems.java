/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.borders;

import javax.swing.border.BevelBorder;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;
/**
 * 
 * BevelBorderTypeItems
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class BevelBorderTypeItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("LOWERED", BevelBorder.LOWERED, "javax.swing.border.BevelBorder.LOWERED"),
			new Item("RAISED", BevelBorder.RAISED, "javax.swing.border.BevelBorder.RAISED") };

	public BevelBorderTypeItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}
