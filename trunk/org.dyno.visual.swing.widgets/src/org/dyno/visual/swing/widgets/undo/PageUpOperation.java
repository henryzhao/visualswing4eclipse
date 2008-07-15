/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.widgets.undo;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class PageUpOperation extends MoveOperation {
	public PageUpOperation(WidgetAdapter adapter) {
		super(adapter);
	}

	@Override
	protected int getStepSize() {
		return -20;
	}

	@Override
	protected boolean isVertical() {
		return true;
	}

	@Override
	protected String getName() {
		return "Page Up";
	}

}
