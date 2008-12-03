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

package org.dyno.visual.swing.base;

import java.awt.Component;
import java.awt.Graphics;
import java.util.List;

import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.IContextCustomizer;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.action.MenuManager;

public class ContextCustomizerAdapter implements IContextCustomizer {

	@Override
	public void fillContextMenu(MenuManager menuManager,
			WidgetAdapter rootAdapter, List<Component> selected) {
	}

	@Override
	public void fillIAdapterMenu(MenuManager manager,
			WidgetAdapter rootAdapter, List<IAdapter> iadapters) {
	}

	@Override
	public void fillInvisibleAdapterMenu(MenuManager menuManager,
			WidgetAdapter rootAdapter, List<InvisibleAdapter> selected) {
	}

	@Override
	public void fillInvisibleRootMenu(MenuManager menuManager,
			WidgetAdapter rootAdapter) {
	}

	@Override
	public void paintContext(Graphics g, WidgetAdapter rootAdapter) {
	}

}

