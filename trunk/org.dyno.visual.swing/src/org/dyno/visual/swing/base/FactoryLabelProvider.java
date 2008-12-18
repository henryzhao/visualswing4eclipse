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

import org.eclipse.jface.viewers.LabelProvider;
/**
 * 
 * FactoryLabelProvider
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class FactoryLabelProvider extends LabelProvider {
	private FactoryItem[] items;
	private IFactoryProvider provider;

	public FactoryLabelProvider(IFactoryProvider provider) {
		this.provider = provider;
		this.items = provider.getItems();
	}

	@Override
	public String getText(Object element) {
		for (int i = 0; i < items.length; i++) {
			if (provider.isSelected(items[i], element)) {
				return items[i].getObjectName();
			}
		}
		return items[0].getObjectName();
	}
}

