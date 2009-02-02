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

import org.dyno.visual.swing.plugin.spi.AbstractAdaptable;
import org.dyno.visual.swing.plugin.spi.IValueParser;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

@SuppressWarnings("unchecked")
public class PropertyAdapter extends AbstractAdaptable implements IWidgetPropertyDescriptor {

	@Override
	public boolean cloneProperty(Object bean, Component clone) {
		return false;
	}

	@Override
	public Object getPropertyValue(IStructuredSelection bean) {
		return null;
	}

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
	public boolean isPropertyResettable(IStructuredSelection bean) {
		return false;
	}

	@Override
	public boolean isPropertySet(String lnfClassname, IStructuredSelection bean) {
		return false;
	}

	@Override
	public void resetPropertyValue(String lnfClassname, IStructuredSelection bean) {
	}

	@Override
	public void setBean(IStructuredSelection bean) {
	}

	@Override
	public void setCategory(String categoryName) {
	}

	@Override
	public void setFilterFlags(String[] filters) {
	}

	@Override
	public void setPropertyValue(IStructuredSelection bean, Object value) {
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

	@Override
	public IValueParser getValueParser() {
		return null;
	}

	@Override
	public Object getFieldValue(Object bean) {
		return null;
	}

	@Override
	public void setFieldValue(Object bean, Object newValue) {
	}

	
	@Override
	protected Class getObjectClass() {
		return getClass();
	}

	
	@Override
	public Class getPropertyType() {
		return getClass();
	}
}

