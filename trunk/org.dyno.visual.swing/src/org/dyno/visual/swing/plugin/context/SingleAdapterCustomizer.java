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

package org.dyno.visual.swing.plugin.context;

import java.awt.Component;
import java.util.List;

import org.dyno.visual.swing.base.ContextCustomizerAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.action.MenuManager;

public class SingleAdapterCustomizer extends ContextCustomizerAdapter {
	@Override
	public void fillContextMenu(MenuManager menuManager, WidgetAdapter rootAdapter, List<Component> selected) {
		if(selected.size()==1){
			Component context_comp = selected.get(0);
			WidgetAdapter contextAdapter=WidgetAdapter.getWidgetAdapter(context_comp);
			if(contextAdapter!=null)
				contextAdapter.fillContextAction(menuManager);
		}
	}
}

