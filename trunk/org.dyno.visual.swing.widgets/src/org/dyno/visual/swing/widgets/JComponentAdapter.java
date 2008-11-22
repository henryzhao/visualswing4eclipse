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

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JComponentAdapter extends WidgetAdapter {

	protected JComponentAdapter() {
	}

	protected Component createWidget() {
		return null;
	}

	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		return null;
	}

	@Override
	protected Component newWidget() {
		return null;
	}
}