

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

import javax.swing.JDesktopPane;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class DesktopPaneDragModeItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("LIVE_DRAG_MODE", JDesktopPane.LIVE_DRAG_MODE, "javax.swing.JDesktopPane.LIVE_DRAG_MODE"),
			new Item("OUTLINE_DRAG_MODE", JDesktopPane.OUTLINE_DRAG_MODE, "javax.swing.JDesktopPane.OUTLINE_DRAG_MODE") };

	public DesktopPaneDragModeItems() {
	}

	
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}

