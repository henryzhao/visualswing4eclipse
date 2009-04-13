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

package org.dyno.visual.swing.designer;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.IStructuredSelection;
/**
 * 
 * WidgetSelection
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
@SuppressWarnings("unchecked")
public class WidgetSelection extends ArrayList<Component> implements IStructuredSelection {
	private static final long serialVersionUID = -7010445264838695570L;

	public WidgetSelection(Component comp) {
		addSelection(comp);
	}

	private void addSelection(Component comp) {
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(comp);
		if (adapter == null)
			return;
		if (adapter.isSelected())
			add(adapter.getWidget());
		if(comp instanceof JComponent){
			JComponent jcomp = (JComponent) comp;
			JPopupMenu jpopup = JavaUtil.getComponentPopupMenu(jcomp);
			if(jpopup!=null&&WidgetAdapter.getWidgetAdapter(jpopup)!=null){
				addSelection(jpopup);
			}
		}
		if (adapter instanceof CompositeAdapter) {
			CompositeAdapter compositeAdapter = (CompositeAdapter) adapter;
			int count = compositeAdapter.getChildCount();
			for (int i = 0; i < count; i++) {
				comp = compositeAdapter.getChild(i);
				addSelection(comp);
			}
		}
	}

	
	public Object getFirstElement() {
		return isEmpty()?null:get(0);
	}

	
	public List toList() {
		return this;
	}
}

