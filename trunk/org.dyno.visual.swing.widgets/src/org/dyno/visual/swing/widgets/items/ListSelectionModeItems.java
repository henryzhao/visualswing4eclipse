
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

import javax.swing.ListSelectionModel;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class ListSelectionModeItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = {
			new Item("SINGLE_SELECTION", ListSelectionModel.SINGLE_SELECTION, "javax.swing.ListSelectionModel.SINGLE_SELECTION"),
			new Item("SINGLE_INTERVAL_SELECTION", ListSelectionModel.SINGLE_INTERVAL_SELECTION, "javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION"),
			new Item("MULTIPLE_INTERVAL_SELECTION", ListSelectionModel.MULTIPLE_INTERVAL_SELECTION,
					"javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION") };

	public ListSelectionModeItems() {
	}

	
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}

