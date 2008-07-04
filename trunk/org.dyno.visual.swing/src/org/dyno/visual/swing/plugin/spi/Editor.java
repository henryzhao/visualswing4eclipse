/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.plugin.spi;

import java.awt.Component;
import java.awt.Font;

import javax.swing.event.ChangeListener;
/**
 * 
 * Editor
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public interface Editor {
	void validateValue() throws Exception;

	void setFocus();

	Object getValue();

	void setValue(Object v);

	Component getComponent();

	void addChangeListener(ChangeListener l);

	void removeChangeListener(ChangeListener l);
	
	void setFont(Font f);
}
