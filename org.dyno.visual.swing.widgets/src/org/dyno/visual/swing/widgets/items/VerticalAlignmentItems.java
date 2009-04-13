
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

	
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}

