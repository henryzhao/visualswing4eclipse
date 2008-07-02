/*
 * TabLayoutPolicyWrapper.java
 *
 * Created on 2007-9-2, 13:56:24
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dyno.visual.swing.widgets.items;

import javax.swing.JTabbedPane;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class TabPlacementItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("TOP", JTabbedPane.TOP, "javax.swing.JTabbedPane.TOP"),
			new Item("LEFT", JTabbedPane.LEFT, "javax.swing.JTabbedPane.LEFT"), new Item("BOTTOM", JTabbedPane.BOTTOM, "javax.swing.JTabbedPane.BOTTOM"),
			new Item("RIGHT", JTabbedPane.RIGHT, "javax.swing.JTabbedPane.RIGHT") };

	public TabPlacementItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}