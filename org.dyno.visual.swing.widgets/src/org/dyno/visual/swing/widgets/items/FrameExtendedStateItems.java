
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

import java.awt.Frame;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class FrameExtendedStateItems implements ItemProvider {

	private Item[] VALUE_ITEMS = {
			new Item("NORMAL", Frame.NORMAL, "java.awt.Frame.NORMAL"),
			new Item("ICONIFIED", Frame.ICONIFIED, "java.awt.Frame.ICONIFIED"),
			new Item("MAXIMIZED_HORIZ", Frame.MAXIMIZED_HORIZ, "java.awt.Frame.MAXIMIZED_HORIZ"),
			new Item("MAXIMIZED_VERT", Frame.MAXIMIZED_VERT, "java.awt.Frame.MAXIMIZED_VERT"),
			new Item("MAXIMIZED_BOTH", Frame.MAXIMIZED_BOTH, "java.awt.Frame.MAXIMIZED_BOTH"),
	};

	public FrameExtendedStateItems() {
	}

	
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}

