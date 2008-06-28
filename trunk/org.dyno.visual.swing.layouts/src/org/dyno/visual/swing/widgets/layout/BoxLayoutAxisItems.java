/*
 * HorizontalAlignmentWrapper.java
 *
 * Created on 2007-8-19, 16:48:45
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dyno.visual.swing.widgets.layout;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class BoxLayoutAxisItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { 
		new Item("X_AXIS", javax.swing.BoxLayout.X_AXIS, "javax.swing.BoxLayout.X_AXIS"),
		new Item("Y_AXIS", javax.swing.BoxLayout.Y_AXIS, "javax.swing.BoxLayout.Y_AXIS"),
		new Item("LINE_AXIS", javax.swing.BoxLayout.LINE_AXIS, "javax.swing.BoxLayout.LINE_AXIS"),
		new Item("PAGE_AXIS", javax.swing.BoxLayout.PAGE_AXIS, "javax.swing.BoxLayout.PAGE_AXIS"),
	};

	public BoxLayoutAxisItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}