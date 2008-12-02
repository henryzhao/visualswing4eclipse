/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.Container;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class ContainerAdapter extends WidgetAdapter {

	public ContainerAdapter() {
	}

	@Override
	protected Component createWidget() {
		return null;
	}

	@Override
	protected Component newWidget() {
		return null;
	}

	@Override
	public Class getWidgetClass() {
		return Container.class;
	}

}
