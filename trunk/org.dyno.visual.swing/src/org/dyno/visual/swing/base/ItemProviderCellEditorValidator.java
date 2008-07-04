/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.base;

import org.eclipse.jface.viewers.ICellEditorValidator;
/**
 * 
 * ItemProviderCellEditorValidator
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class ItemProviderCellEditorValidator implements ICellEditorValidator {
	private ItemProvider provider;

	public ItemProviderCellEditorValidator(ItemProvider provider) {
		this.provider = provider;
	}

	@Override
	public String isValid(Object value) {
		if (value == null)
			return null;
		int index = ((Integer) value).intValue();
		if (index < 0 || index >= provider.getItems().length)
			return "Cannot find such item:{0}!";
		return null;
	}
}
