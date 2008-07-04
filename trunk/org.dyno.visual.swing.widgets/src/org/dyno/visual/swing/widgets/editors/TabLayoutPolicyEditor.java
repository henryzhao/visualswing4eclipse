/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.editors;

import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.widgets.items.TabLayoutPolicyItems;

public class TabLayoutPolicyEditor extends ItemProviderCellEditorFactory{

	public TabLayoutPolicyEditor() {
		super(new TabLayoutPolicyItems());
	}
}
