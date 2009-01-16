
/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.widgets;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class JLabelAdapter extends TextWidgetAdapter {

	@Override
	@SuppressWarnings("unchecked")
	public Class getWidgetClass() {
		return JLabel.class;
	}

	@Override
	protected Dimension getPreferredInitialSize(JComponent jc) {
		Dimension size= super.getPreferredInitialSize(jc);
		size.width += 8;
		size.height += 6;
		return size;
	}
}

