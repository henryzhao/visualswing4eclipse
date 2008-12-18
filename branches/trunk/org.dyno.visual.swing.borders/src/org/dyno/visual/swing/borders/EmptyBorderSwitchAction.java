
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
import javax.swing.border.EmptyBorder;

import org.eclipse.jface.action.Action;
/**
 * 
 * EmptyBorderSwitchAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
class EmptyBorderSwitchAction extends Action {
	private JComponent target;

	public EmptyBorderSwitchAction(JComponent w) {
		super("Empty Border", AS_RADIO_BUTTON);
		target = w;
		setId("empty_border_switch_action");
		Border border = w.getBorder();
		setChecked(border != null && border.getClass() == EmptyBorder.class);
	}

	@Override
	public void run() {
		Border border = target.getBorder();
		if (border == null || border.getClass() != EmptyBorder.class) {
			target.setBorder(BorderFactory.createEmptyBorder());
			target.repaint();
		}
		setChecked(true);
	}
}

