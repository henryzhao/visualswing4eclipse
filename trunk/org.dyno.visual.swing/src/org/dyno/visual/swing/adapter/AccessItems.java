/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.adapter;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class AccessItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = {
			new Item("private", WidgetAdapter.ACCESS_PRIVATE, "private"),
			new Item("package", WidgetAdapter.ACCESS_DEFAULT, ""),
			new Item("protected", WidgetAdapter.ACCESS_PROTECTED, "protected"),
			new Item("public", WidgetAdapter.ACCESS_PUBLIC, "public") };

	public AccessItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}
