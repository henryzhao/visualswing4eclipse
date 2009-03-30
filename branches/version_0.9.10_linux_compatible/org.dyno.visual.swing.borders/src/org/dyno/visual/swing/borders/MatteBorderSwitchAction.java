
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
import javax.swing.border.MatteBorder;

import org.eclipse.jface.action.Action;

class MatteBorderSwitchAction extends Action {
	private JComponent target;

	public MatteBorderSwitchAction(JComponent w) {
		super("Matte Border", AS_RADIO_BUTTON);
		target = w;
		setId("matte_border_switch_action");
		Border border = w.getBorder();
		setChecked(border != null && border.getClass() == MatteBorder.class);
	}

	@Override
	public void run() {
		Border border = target.getBorder();
		if (border == null || border.getClass() != MatteBorder.class) {
			target.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.black));
			target.repaint();
		}
		setChecked(true);
	}
}

