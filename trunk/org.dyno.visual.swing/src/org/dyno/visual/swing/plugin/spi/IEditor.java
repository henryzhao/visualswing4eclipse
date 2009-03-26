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

package org.dyno.visual.swing.plugin.spi;

import java.awt.Component;
import java.awt.Font;

import javax.swing.event.ChangeListener;
/**
 * 
 * IEditor
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public interface IEditor {
	void validateValue() throws Exception;

	void setFocus();

	Object getValue();

	void setValue(Object v);

	Component getComponent();

	void addChangeListener(ChangeListener l);

	void removeChangeListener(ChangeListener l);
	
	void setFont(Font f);

	Object getOldValue();
}

