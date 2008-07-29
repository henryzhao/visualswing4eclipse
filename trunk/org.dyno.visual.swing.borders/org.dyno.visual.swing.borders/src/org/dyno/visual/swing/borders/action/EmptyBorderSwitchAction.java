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
import javax.swing.border.EmptyBorder;

import org.dyno.visual.swing.base.IFactory;
/**
 * 
 * EmptyBorderSwitchAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class EmptyBorderSwitchAction extends BorderSwitchAction {
	public EmptyBorderSwitchAction(JComponent w) {
		super(w, EmptyBorder.class, new IFactory(){
			@Override
			public Object newInstance(Object bean) {
				return BorderFactory.createEmptyBorder();
			}});
	}
}
