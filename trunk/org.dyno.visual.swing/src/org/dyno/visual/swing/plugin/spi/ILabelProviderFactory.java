/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.plugin.spi;

import org.eclipse.jface.viewers.ILabelProvider;
/**
 * 
 * ILabelProviderFactory
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public interface ILabelProviderFactory {
	ILabelProvider getLabelProvider();
}
