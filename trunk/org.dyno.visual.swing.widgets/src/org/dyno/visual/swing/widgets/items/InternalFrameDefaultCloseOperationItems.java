
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

import javax.swing.WindowConstants;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author William Chen
 */
public class InternalFrameDefaultCloseOperationItems implements ItemProvider {

	private Item[] VALUE_ITEMS = { 
			new Item("DO_NOTHING", WindowConstants.DO_NOTHING_ON_CLOSE, "javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE"),
			new Item("HIDE", WindowConstants.HIDE_ON_CLOSE, "javax.swing.WindowConstants.HIDE_ON_CLOSE"),
			new Item("DISPOSE", WindowConstants.DISPOSE_ON_CLOSE, "javax.swing.WindowConstants.DISPOSE_ON_CLOSE") };

	public InternalFrameDefaultCloseOperationItems() {
	}

	
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}

