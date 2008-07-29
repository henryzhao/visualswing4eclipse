/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.items;

import javax.swing.WindowConstants;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class InternalFrameDefaultCloseOperationItems implements ItemProvider {

	private Item[] VALUE_ITEMS = { new Item("DO_NOTHING", WindowConstants.DO_NOTHING_ON_CLOSE, "javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE"),
			new Item("HIDE", WindowConstants.HIDE_ON_CLOSE, "javax.swing.WindowConstants.HIDE_ON_CLOSE"),
			new Item("DISPOSE", WindowConstants.DISPOSE_ON_CLOSE, "javax.swing.WindowConstants.DISPOSE_ON_CLOSE") };

	public InternalFrameDefaultCloseOperationItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}