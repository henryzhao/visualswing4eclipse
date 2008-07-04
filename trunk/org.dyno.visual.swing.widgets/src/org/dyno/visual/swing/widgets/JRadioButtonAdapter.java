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
import javax.swing.JRadioButton;

public class JRadioButtonAdapter extends TextWidgetAdapter {

	private static final int BOX_THUMB = 16;

	@Override
	protected Class<? extends JComponent> getWidgetClass() {
		return JRadioButton.class;
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		Rectangle bounds = super.getEditorBounds(x, y);
		bounds.x += BOX_THUMB;
		bounds.width -= BOX_THUMB;
		return bounds;
	}
}
