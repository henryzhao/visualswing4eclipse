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

import org.eclipse.ui.views.properties.ComboBoxLabelProvider;
/**
 * 
 * ItemProviderLabelProvider
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class ItemProviderLabelProvider extends ComboBoxLabelProvider {
	public ItemProviderLabelProvider(String[] values) {
		super(values);
	}
}

