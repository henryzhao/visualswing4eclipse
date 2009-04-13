
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

import javax.swing.JSplitPane;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

public class SplitPaneConstraintsItems implements ItemProvider {

	private static Item[] HORIZONTAL_ITEMS = { new Item("left", "left", "\"left\""), new Item("right", "right", "\"right\"") };
	private static Item[] VERTICAL_ITEMS = { new Item("top", "top", "\"top\""), new Item("bottom", "bottom", "\"bottom\"") };

	private JSplitPane splitPane;

	public SplitPaneConstraintsItems(JSplitPane jsp) {
		this.splitPane = jsp;
	}

	
	public Item[] getItems() {
		return splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT ? HORIZONTAL_ITEMS : VERTICAL_ITEMS;
	}

}

