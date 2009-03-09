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

import org.dyno.visual.swing.base.PropertyAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;

public class FieldAccessProperty extends PropertyAdapter {
	private AccessEditor editorFactory = new AccessEditor();
	private AccessRenderer rendererFactory = new AccessRenderer();
	private WidgetAdapter adapter;

	public FieldAccessProperty(WidgetAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public Object getPropertyValue(IStructuredSelection bean) {
		return adapter.getFieldAccess();
	}

	@Override
	public void setPropertyValue(IStructuredSelection bean, Object value) {
		adapter.setFieldAccess(value == null ? WidgetAdapter.ACCESS_PRIVATE
				: (Integer) value);
		adapter.setDirty(true);
		adapter.changeNotify();
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return editorFactory.createPropertyEditor(adapter, parent);
	}

	@Override
	public String getCategory() {
		return Messages.FIELD_ACCESS_PROPERTY_CATEGORY;
	}

	@Override
	public String getDisplayName() {
		return Messages.FIELD_ACCESS_PROPERTY_DISPLAY_NAME+TEXT_TRAILING;
	}

	@Override
	public Object getId() {
		return "field_access"; //$NON-NLS-1$
	}

	@Override
	public ILabelProvider getLabelProvider() {
		return rendererFactory.getLabelProvider();
	}
}

