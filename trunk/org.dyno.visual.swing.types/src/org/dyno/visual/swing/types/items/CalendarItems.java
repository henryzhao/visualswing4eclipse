/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.items;

import java.util.Calendar;

import org.dyno.visual.swing.base.Item;
import org.dyno.visual.swing.base.ItemProvider;

/**
 * 
 * @author rehte
 */
public class CalendarItems implements ItemProvider {

	private static Item[] VALUE_ITEMS = { new Item("Era", Calendar.ERA, "java.util.Calendar.ERA"), new Item("Year", Calendar.YEAR, "java.util.Calendar.YEAR"),
			new Item("Month", Calendar.MONTH, "java.util.Calendar.MONTH"), new Item("Week of Year", Calendar.WEEK_OF_YEAR, "java.util.Calendar.WEEK_OF_YEAR"),
			new Item("Week of Month", Calendar.WEEK_OF_MONTH, "java.util.Calendar.WEEK_OF_MONTH"),
			new Item("Day of Month", Calendar.DAY_OF_MONTH, "java.util.Calendar.DAY_OF_MONTH"),
			new Item("Day of Year", Calendar.DAY_OF_YEAR, "java.util.Calendar.DAY_OF_YEAR"),
			new Item("Day of Week", Calendar.DAY_OF_WEEK, "java.util.Calendar.DAY_OF_WEEK"),
			new Item("Day of Week in Month", Calendar.DAY_OF_WEEK_IN_MONTH, "java.util.Calendar.DAY_OF_WEEK_IN_MONTH"),
			new Item("AM/PM", Calendar.AM_PM, "java.util.Calendar.AM_PM"), new Item("Hour", Calendar.HOUR, "java.util.Calendar.HOUR"),
			new Item("Hour of Day", Calendar.HOUR_OF_DAY, "java.util.Calendar.HOUR_OF_DAY"), new Item("Minute", Calendar.MINUTE, "java.util.Calendar.MINUTE"),
			new Item("Second", Calendar.SECOND, "java.util.Calendar.SECOND"), new Item("Millisecond", Calendar.MILLISECOND, "java.util.Calendar.MILLISECOND") };

	@Override
	public Item[] getItems() {
		return VALUE_ITEMS;
	}
}