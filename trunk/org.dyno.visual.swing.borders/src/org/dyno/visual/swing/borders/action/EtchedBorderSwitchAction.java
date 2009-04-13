
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

package org.dyno.visual.swing.borders.action;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.EtchedBorder;

import org.dyno.visual.swing.base.IFactory;

public class EtchedBorderSwitchAction extends BorderSwitchAction {
	public EtchedBorderSwitchAction(JComponent w) {
		super(w, EtchedBorder.class, new IFactory(){
			
			public Object newInstance(Object bean) {
				return BorderFactory.createEtchedBorder();
			}});
	}
}

