/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JSeparator;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JSeparatorAdapter extends WidgetAdapter {
	private static int VAR_INDEX = 0;

	public JSeparatorAdapter() {
		super("jSeparator" + (VAR_INDEX++));
	}

	@Override
	protected JComponent createWidget() {
		JSeparator separator = new JSeparator();
		Dimension size = new Dimension(100, 10);
		separator.setSize(size);
		separator.doLayout();
		separator.validate();
		return separator;
	}

	@Override
	protected JComponent newWidget() {
		return new JSeparator();
	}
}
