/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.borders.action;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.EtchedBorder;

import org.dyno.visual.swing.base.IFactory;

public class EtchedBorderSwitchAction extends BorderSwitchAction {
	public EtchedBorderSwitchAction(JComponent w) {
		super(w, EtchedBorder.class, new IFactory(){
			@Override
			public Object newInstance(Object bean) {
				return BorderFactory.createEtchedBorder();
			}});
	}
}
