
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

package org.dyno.visual.swing.borders;

import javax.swing.border.EtchedBorder;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

public class EtchedBorderTypeItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("RAISED", EtchedBorder.RAISED, "javax.swing.border.EtchedBorder.RAISED"),
			new Item("LOWERED", EtchedBorder.LOWERED, "javax.swing.border.EtchedBorder.LOWERED") };

	public EtchedBorderTypeItems() {
	}

	
	public Item[] getItems() {
		return VALUE_ITEMS;
	}

}

