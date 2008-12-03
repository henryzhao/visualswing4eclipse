
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

package org.dyno.visual.swing.borders;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.eclipse.jface.action.Action;

class LineBorderSwitchAction extends Action {
	private JComponent target;

	public LineBorderSwitchAction(JComponent w) {
		super("Line Border", AS_RADIO_BUTTON);
		target = w;
		setId("line_border_switch_action");
		Border border = w.getBorder();
		setChecked(border != null && border.getClass() == LineBorder.class);
	}

	@Override
	public void run() {
		Border border = target.getBorder();
		if (border == null || border.getClass() != LineBorder.class) {
			target.setBorder(BorderFactory.createLineBorder(Color.black));
			target.repaint();
		}
		setChecked(true);
	}
}

