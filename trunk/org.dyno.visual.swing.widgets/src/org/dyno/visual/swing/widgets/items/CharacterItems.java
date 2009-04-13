
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

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

public class CharacterItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { 
		new Item("A", (int) 'A', "\'A\'"), 
		new Item("B", (int) 'B', "\'B\'"), 
		new Item("C", (int) 'C', "\'C\'"),
			new Item("D", (int) 'D', "\'D\'"), new Item("E", (int) 'E', "\'E\'"), new Item("F", (int) 'F', "\'F\'"), new Item("G", (int) 'G', "\'G\'"),
			new Item("H", (int) 'H', "\'H\'"), new Item("I", (int) 'I', "\'I\'"), new Item("J", (int) 'J', "\'J\'"), new Item("K", (int) 'K', "\'K\'"),
			new Item("L", (int) 'L', "\'L\'"), new Item("M", (int) 'M', "\'M\'"), new Item("N", (int) 'N', "\'N\'"), new Item("O", (int) 'O', "\'O\'"),
			new Item("P", (int) 'P', "\'P\'"), new Item("Q", (int) 'Q', "\'Q\'"), new Item("R", (int) 'R', "\'R\'"), new Item("S", (int) 'S', "\'S\'"),
			new Item("T", (int) 'T', "\'T\'"), new Item("U", (int) 'U', "\'U\'"), new Item("V", (int) 'V', "\'V\'"), new Item("W", (int) 'W', "\'W\'"),
			new Item("X", (int) 'X', "\'X\'"), new Item("Y", (int) 'Y', "\'Y\'"), new Item("Z", (int) 'Z', "\'Z\'"), new Item("0", (int) '0', "\'0\'"),
			new Item("1", (int) '1', "\'1\'"), new Item("2", (int) '2', "\'2\'"), new Item("3", (int) '3', "\'3\'"), new Item("4", (int) '4', "\'4\'"),
			new Item("5", (int) '5', "\'5\'"), new Item("6", (int) '6', "\'6\'"), new Item("7", (int) '7', "\'7\'"), new Item("8", (int) '8', "\'8\'"),
			new Item("9", (int) '9', "\'9\'"), };

	public CharacterItems() {
	}

	
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}

