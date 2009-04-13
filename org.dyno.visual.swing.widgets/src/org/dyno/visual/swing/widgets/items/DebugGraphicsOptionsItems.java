

/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.widgets.items;

import javax.swing.DebugGraphics;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class DebugGraphicsOptionsItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("NONE_OPTION", DebugGraphics.NONE_OPTION, "javax.swing.DebugGraphics.NONE_OPTION"),
			new Item("NO_CHANGES", 0, "0"), new Item("LOG_OPTION", DebugGraphics.LOG_OPTION, "javax.swing.DebugGraphics.LOG_OPTION"),
			new Item("FLASH_OPTION", DebugGraphics.FLASH_OPTION, "javax.swing.DebugGraphics.FLASH_OPTION"),
			new Item("BUFFERED_OPTION", DebugGraphics.BUFFERED_OPTION, "javax.swing.DebugGraphics.BUFFERED_OPTION") };

	public DebugGraphicsOptionsItems() {
	}

	
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}

