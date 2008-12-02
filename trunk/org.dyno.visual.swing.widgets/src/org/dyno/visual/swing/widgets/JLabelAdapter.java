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
import javax.swing.JLabel;

public class JLabelAdapter extends TextWidgetAdapter {

	@Override
	public Class getWidgetClass() {
		return JLabel.class;
	}

}
