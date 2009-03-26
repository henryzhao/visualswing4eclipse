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

import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.eclipse.jface.viewers.ILabelProvider;
/**
 * 
 * FactoryRenderer
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class FactoryRenderer implements ILabelProviderFactory {
	private static final long serialVersionUID = -4403435758517308113L;
	private FactoryLabelProvider labelProvider;
	private IFactoryProvider provider;
	public FactoryRenderer(IFactoryProvider provider){
		this.provider = provider;
	}
	@Override
	public ILabelProvider getLabelProvider() {
		if (labelProvider == null)
			labelProvider = new FactoryLabelProvider(provider);
		return labelProvider;
	}
}

