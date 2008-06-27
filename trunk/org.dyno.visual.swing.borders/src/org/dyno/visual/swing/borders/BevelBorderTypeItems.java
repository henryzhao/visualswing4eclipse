package org.dyno.visual.swing.borders;

import javax.swing.border.BevelBorder;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

public class BevelBorderTypeItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("LOWERED", BevelBorder.LOWERED, "javax.swing.border.BevelBorder.LOWERED"),
			new Item("RAISED", BevelBorder.RAISED, "javax.swing.border.BevelBorder.RAISED") };

	public BevelBorderTypeItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}
