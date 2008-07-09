/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.adapter;

import org.dyno.visual.swing.base.ItemProviderCellEditorFactory;


public class AccessEditor extends ItemProviderCellEditorFactory {
	public AccessEditor() {
		super(new AccessItems());
	}
}
