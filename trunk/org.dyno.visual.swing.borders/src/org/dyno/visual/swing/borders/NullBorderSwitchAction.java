
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

import javax.swing.JComponent;
import javax.swing.border.Border;

import org.eclipse.jface.action.Action;

class NullBorderSwitchAction extends Action {
	private JComponent target;

	public NullBorderSwitchAction(JComponent w) {
		super("Null Border", AS_RADIO_BUTTON);
		target = w;
		setId("null_border_switch_action");
		Border border = w.getBorder();
		setChecked(border == null);
	}

	@Override
	public void run() {
		target.setBorder(null);
		target.repaint();
		setChecked(true);
	}
}

