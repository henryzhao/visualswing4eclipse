/*
 * ListSelectionModeWrapper.java
 *
 * Created on 2007-8-29, 0:30:57
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}