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
			return Messages.ITEMP_PROVIDER_CELL_EDITOR_VALIDATOR_CANNOT_FIND_SUCH_ITEM;
		return null;
	}
}

