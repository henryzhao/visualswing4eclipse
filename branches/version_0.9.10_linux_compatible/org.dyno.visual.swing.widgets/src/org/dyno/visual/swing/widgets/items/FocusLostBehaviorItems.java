

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

