

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

