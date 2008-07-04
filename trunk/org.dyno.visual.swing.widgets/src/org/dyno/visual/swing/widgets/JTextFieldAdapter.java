/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class JTextFieldAdapter extends TextWidgetAdapter {

	@Override
	protected Class<? extends JComponent> getWidgetClass() {
		return JTextField.class;
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = getWidget().getWidth();
		int h = getWidget().getHeight();
		return new Rectangle(0, 0, w, h);
	}

}
