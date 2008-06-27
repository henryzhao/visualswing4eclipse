/*
 * AutoResizeModeWrapper.java
 *
 * Created on 2007-9-2, 19:59:15
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dyno.visual.swing.widgets.items;

import javax.swing.JTable;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class AutoResizeModeItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("OFF", JTable.AUTO_RESIZE_OFF, "javax.swing.JTable.AUTO_RESIZE_OFF"),
			new Item("NEXT_COLUMN", JTable.AUTO_RESIZE_NEXT_COLUMN, "javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN"),
			new Item("SUBSEQUENT_COLUMNS", JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS, "javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS"),
			new Item("LAST_COLUMN", JTable.AUTO_RESIZE_LAST_COLUMN, "javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN"),
			new Item("ALL_COLUMNS", JTable.AUTO_RESIZE_ALL_COLUMNS, "javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS") };

	public AutoResizeModeItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}