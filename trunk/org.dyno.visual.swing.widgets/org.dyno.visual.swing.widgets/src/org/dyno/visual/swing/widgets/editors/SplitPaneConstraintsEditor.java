/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets.editors;

import javax.swing.JSplitPane;

import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;
import org.dyno.visual.swing.widgets.items.SplitPaneConstraintsItems;

public class SplitPaneConstraintsEditor extends ItemProviderCellEditorFactory {
	private static final long serialVersionUID = 1L;

	public SplitPaneConstraintsEditor(JSplitPane jsp) {
		super(new SplitPaneConstraintsItems(jsp));
	}
}
