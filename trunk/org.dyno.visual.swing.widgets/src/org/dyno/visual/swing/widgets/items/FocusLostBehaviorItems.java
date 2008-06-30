/*
 * FocusLostBehaviorWrapper.java
 *
 * Created on 2007-8-29, 0:04:12
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dyno.visual.swing.widgets.items;

import javax.swing.JFormattedTextField;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class FocusLostBehaviorItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("COMMIT", JFormattedTextField.COMMIT, "javax.swing.JFormattedTextField.COMMIT"),
			new Item("COMMIT_OR_REVERT", JFormattedTextField.COMMIT_OR_REVERT, "javax.swing.JFormattedTextField.COMMIT_OR_REVERT"),
			new Item("REVERT", JFormattedTextField.REVERT, "javax.swing.JFormattedTextField.REVERT"),
			new Item("PERSIST", JFormattedTextField.PERSIST, "javax.swing.JFormattedTextField.PERSIST") };

	public FocusLostBehaviorItems() {
	}

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}