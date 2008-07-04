/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.widgets;

import javax.swing.JComponent;
import javax.swing.JToggleButton;

public class JToggleButtonAdapter extends TextWidgetAdapter {

	@Override
	protected Class<? extends JComponent> getWidgetClass() {
		return JToggleButton.class;
	}

}
