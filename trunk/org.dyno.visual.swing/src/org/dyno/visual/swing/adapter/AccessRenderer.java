/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.adapter;

import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;

public class AccessRenderer extends ItemProviderLabelProviderFactory {
	public AccessRenderer() {
		super(new AccessItems());
	}
}