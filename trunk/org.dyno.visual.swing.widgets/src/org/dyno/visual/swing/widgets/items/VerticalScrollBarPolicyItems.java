
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

import javax.swing.ScrollPaneConstants;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class VerticalScrollBarPolicyItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = {
			new Item("AS_NEEDED", ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, "javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED"),
			new Item("NEVER", ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, "javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER"),
			new Item("ALWAYS", ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, "javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS") };

	public VerticalScrollBarPolicyItems() {
	}

	
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}

