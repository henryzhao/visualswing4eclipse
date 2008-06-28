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

	@Override
	public Item[] getItems() {
		return splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT ? HORIZONTAL_ITEMS : VERTICAL_ITEMS;
	}

}