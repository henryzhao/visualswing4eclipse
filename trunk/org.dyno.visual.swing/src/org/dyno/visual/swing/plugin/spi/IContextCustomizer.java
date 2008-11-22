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
import java.awt.Graphics;
import java.util.List;

import org.eclipse.jface.action.MenuManager;

public interface IContextCustomizer {
	void fillContextMenu(MenuManager menuManager, WidgetAdapter rootAdapter, List<Component>selected);
	void fillInvisibleRootMenu(MenuManager menuManager, WidgetAdapter rootAdapter);
	void fillInvisibleAdapterMenu(MenuManager menuManager, WidgetAdapter rootAdapter, List<InvisibleAdapter> selected);
	void fillIAdapterMenu(MenuManager manager, WidgetAdapter rootAdapter, List<IAdapter> iadapters);
	void paintContext(Graphics g, WidgetAdapter rootAdapter);
}
