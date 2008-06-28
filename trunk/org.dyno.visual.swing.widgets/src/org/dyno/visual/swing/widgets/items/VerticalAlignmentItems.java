/*
 * HorizontalAlignmentWrapper.java
 *
 * Created on 2007-8-19, 16:48:45
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dyno.visual.swing.widgets.items;

import javax.swing.SwingConstants;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class VerticalAlignmentItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("TOP", SwingConstants.TOP, "javax.swing.SwingConstants.TOP"),
			new Item("CENTER", SwingConstants.CENTER, "javax.swing.SwingConstants.CENTER"),
			new Item("BOTTOM", SwingConstants.BOTTOM, "javax.swing.SwingConstants.BOTTOM") };

	public VerticalAlignmentItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}
