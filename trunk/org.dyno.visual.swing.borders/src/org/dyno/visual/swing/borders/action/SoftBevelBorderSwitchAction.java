/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.borders.action;

import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import org.dyno.visual.swing.base.IFactory;

public class SoftBevelBorderSwitchAction extends BorderSwitchAction {
	public SoftBevelBorderSwitchAction(JComponent w) {
		super(w, SoftBevelBorder.class, new IFactory(){
			@Override
			public Object newInstance(Object bean) {
				return new SoftBevelBorder(BevelBorder.LOWERED);
			}});
	}
}
