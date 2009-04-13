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

	
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}

