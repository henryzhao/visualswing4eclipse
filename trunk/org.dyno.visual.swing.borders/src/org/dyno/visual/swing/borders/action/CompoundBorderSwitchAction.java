
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
import javax.swing.border.CompoundBorder;

import org.dyno.visual.swing.base.IFactory;
/**
 * 
 * CompoundBorderSwitchAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class CompoundBorderSwitchAction extends BorderSwitchAction {
	public CompoundBorderSwitchAction(JComponent w) {
		super(w, CompoundBorder.class, new IFactory(){
			@Override
			public Object newInstance(Object bean) {
				return BorderFactory.createCompoundBorder();
			}});
	}
}

