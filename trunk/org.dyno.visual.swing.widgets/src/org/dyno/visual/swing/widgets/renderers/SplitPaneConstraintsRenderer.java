/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.renderers;

import javax.swing.JSplitPane;

import org.dyno.visual.swing.base.ItemProviderLabelProviderFactory;
import org.dyno.visual.swing.widgets.items.SplitPaneConstraintsItems;

public class SplitPaneConstraintsRenderer extends ItemProviderLabelProviderFactory {
	private static final long serialVersionUID = 1L;

	public SplitPaneConstraintsRenderer(JSplitPane jsp) {
		super(new SplitPaneConstraintsItems(jsp));
	}

}
