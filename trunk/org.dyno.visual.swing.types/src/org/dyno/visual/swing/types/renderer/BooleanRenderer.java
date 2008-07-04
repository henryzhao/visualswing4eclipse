/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.renderer;

import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.eclipse.jface.viewers.ILabelProvider;

public class BooleanRenderer implements ILabelProviderFactory {
	private static final long serialVersionUID = -4403435758517308113L;
	private BooleanLabelProvider provider;

	@Override
	public ILabelProvider getLabelProvider() {
		if (provider == null)
			provider = new BooleanLabelProvider();
		return provider;
	}
}
