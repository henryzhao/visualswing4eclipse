/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.renderer;

import org.dyno.visual.swing.base.FactoryRenderer;
import org.dyno.visual.swing.types.items.LayoutItems;

public class LayoutRenderer extends FactoryRenderer {

	public LayoutRenderer() {
		super(new LayoutItems());
	}
}
