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

package org.dyno.visual.swing.adapter;

import java.util.List;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.PropertyAdapter;
import org.dyno.visual.swing.plugin.spi.IRenamingListener;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class BeanNameProperty extends PropertyAdapter {
	private WidgetAdapter adapter;
	public BeanNameProperty(WidgetAdapter adapter){
		this.adapter = adapter;
	}
	@Override
	public Object getPropertyValue(IStructuredSelection bean) {
		return adapter.getName();
	}

	@Override
	public void setPropertyValue(IStructuredSelection bean, Object value) {
		String name = (String) value;
		String lastName = adapter.getName();
		adapter.setName(name);
		adapter.setLastName(lastName);
		if (!adapter.isRoot()) {
			adapter.getParentAdapter().setDirty(true);
		}
		adapter.lockDesigner();
		List<IRenamingListener> listeners = ExtensionRegistry.getRenamingListeners();
		for(IRenamingListener listener:listeners){
			listener.adapterRenamed(adapter.getCompilationUnit(), adapter);
		}
		adapter.changeNotify();
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		TextCellEditor editor = new TextCellEditor(parent);
		editor.setValidator(new BeanNameValidator(adapter));
		return editor;
	}

	@Override
	public String getCategory() {
		return "Code";
	}

	@Override
	public String getDisplayName() {
		return "Bean Field Name";
	}

	@Override
	public Object getId() {
		return "bean.name";
	}
}

