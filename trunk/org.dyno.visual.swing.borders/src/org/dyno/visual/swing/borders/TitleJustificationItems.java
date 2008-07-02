/*
 * TitleJustificationWrapper.java
 *
 * Created on 2007-8-28, 0:58:46
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dyno.visual.swing.borders;

import javax.swing.border.TitledBorder;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class TitleJustificationItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = {
			new Item("DEFAULT_JUSTIFICATION", TitledBorder.DEFAULT_JUSTIFICATION, "javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION"),
			new Item("LEFT", TitledBorder.LEFT, "javax.swing.border.TitledBorder.LEFT"),
			new Item("CENTER", TitledBorder.CENTER, "javax.swing.border.TitledBorder.CENTER"),
			new Item("RIGHT", TitledBorder.RIGHT, "javax.swing.border.TitledBorder.RIGHT"),
			new Item("LEADING", TitledBorder.LEADING, "javax.swing.border.TitledBorder.LEADING"),
			new Item("TRAILING", TitledBorder.TRAILING, "javax.swing.border.TitledBorder.TRAILING") };

	public TitleJustificationItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}