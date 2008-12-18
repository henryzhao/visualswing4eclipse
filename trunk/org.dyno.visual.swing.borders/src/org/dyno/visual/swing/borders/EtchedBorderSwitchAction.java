
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

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import org.eclipse.jface.action.Action;

class EtchedBorderSwitchAction extends Action {
	private JComponent target;

	public EtchedBorderSwitchAction(JComponent w) {
		super("Etched Border", AS_RADIO_BUTTON);
		target = w;
		setId("etched_border_switch_action");
		Border border = w.getBorder();
		setChecked(border != null && border.getClass() == EtchedBorder.class);
	}

	@Override
	public void run() {
		Border border = target.getBorder();
		if (border == null || border.getClass() != EtchedBorder.class) {
			target.setBorder(BorderFactory.createEtchedBorder());
			target.repaint();
		}
		setChecked(true);
	}
}

