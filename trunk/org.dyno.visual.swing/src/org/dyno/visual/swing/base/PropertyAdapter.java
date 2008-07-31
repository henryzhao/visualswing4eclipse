/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.base;

import java.awt.Component;

import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class PropertyAdapter implements IWidgetPropertyDescriptor {

	@Override
	public boolean cloneProperty(Object bean, Component clone) {
		return false;
	}

	@Override
	public Object getPropertyValue(Object bean) {
		return null;
	}

	@Override
	public String getSetCode(Object widget, ImportRewrite imports) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(IConfigurationElement config, Class beanClass) {
	}

	@Override
	public boolean isEdited(WidgetAdapter adapter) {
		return false;
	}

	@Override
	public boolean isGencode() {
		return false;
	}

	@Override
	public boolean isPropertyResettable(Object bean) {
		return false;
	}

	@Override
	public boolean isPropertySet(String lnfClassname, Object bean) {
		return false;
	}

	@Override
	public void resetPropertyValue(String lnfClassname, Object bean) {
	}

	@Override
	public void setBean(Object bean) {
	}

	@Override
	public void setCategory(String categoryName) {
	}

	@Override
	public void setFilterFlags(String[] filters) {
	}

	@Override
	public void setPropertyValue(Object bean, Object value) {
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return null;
	}

	@Override
	public String getCategory() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public String[] getFilterFlags() {
		return null;
	}

	@Override
	public Object getHelpContextIds() {
		return null;
	}

	@Override
	public Object getId() {
		return null;
	}

	@Override
	public ILabelProvider getLabelProvider() {
		return null;
	}

	@Override
	public boolean isCompatibleWith(IPropertyDescriptor anotherProperty) {
		return false;
	}
}
