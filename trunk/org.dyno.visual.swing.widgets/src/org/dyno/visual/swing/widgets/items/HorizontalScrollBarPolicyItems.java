/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.items;

import javax.swing.ScrollPaneConstants;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class HorizontalScrollBarPolicyItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = {
			new Item("AS_NEEDED", ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED, "javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED"),
			new Item("NEVER", ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER, "javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER"),
			new Item("ALWAYS", ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS, "javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS") };

	public HorizontalScrollBarPolicyItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}