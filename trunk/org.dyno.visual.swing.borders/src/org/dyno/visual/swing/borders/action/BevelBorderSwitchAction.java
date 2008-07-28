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
import javax.swing.border.BevelBorder;

import org.dyno.visual.swing.base.IFactory;

/**
 * 
 * BevelBorderSwitchAction
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class BevelBorderSwitchAction extends BorderSwitchAction {
	public BevelBorderSwitchAction(JComponent w) {
		super(w, BevelBorder.class, new IFactory(){
			@Override
			public Object newInstance(Object bean) {
				return BorderFactory.createBevelBorder(BevelBorder.LOWERED);
			}});
	}
}
