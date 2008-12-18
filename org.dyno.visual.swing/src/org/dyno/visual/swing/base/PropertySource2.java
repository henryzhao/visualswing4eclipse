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

import java.util.HashMap;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
/**
 * 
 * PropertySource2
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class PropertySource2 implements IPropertySource2 {
	private IStructuredSelection bean;
	private IWidgetPropertyDescriptor[] properties;
	private HashMap<Object, IWidgetPropertyDescriptor> propertyMap;
	private String lnfClassname;
	public PropertySource2(IStructuredSelection bean, IWidgetPropertyDescriptor[] properties) {
		this.bean = bean;
		this.properties = properties;
		this.propertyMap = new HashMap<Object, IWidgetPropertyDescriptor>();
		for (IWidgetPropertyDescriptor property : properties) {
			String id = (String) property.getId();
			this.propertyMap.put(id, property);
			property.setBean(bean);
		}
		LookAndFeel lnf = UIManager.getLookAndFeel();
		if (lnf == null)
			lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
		else
			lnfClassname = lnf.getClass().getName();
	}
	public PropertySource2(String lnfClassname, IStructuredSelection bean, IWidgetPropertyDescriptor[] properties){
		this.lnfClassname = lnfClassname;
		this.bean = bean;
		this.properties = properties;
		this.propertyMap = new HashMap<Object, IWidgetPropertyDescriptor>();
		for (IWidgetPropertyDescriptor property : properties) {
			String id = (String) property.getId();
			this.propertyMap.put(id, property);
			property.setBean(bean);
		}
	}
	@Override
	public boolean isPropertyResettable(Object id) {
		IWidgetPropertyDescriptor property = propertyMap.get(id);
		return property != null && property.isPropertyResettable(bean);
	}

	@Override
	public boolean isPropertySet(Object id) {
		IWidgetPropertyDescriptor property = propertyMap.get(id);
		return property != null && property.isPropertySet(lnfClassname, bean);
	}

	@Override
	public Object getEditableValue() {
		return bean;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return properties;
	}

	@Override
	public Object getPropertyValue(Object id) {
		IWidgetPropertyDescriptor property = propertyMap.get(id);
		return property == null ? null : property.getPropertyValue(bean);
	}

	@Override
	public void resetPropertyValue(Object id) {
		IWidgetPropertyDescriptor property = propertyMap.get(id);
		if (property != null)
			property.resetPropertyValue(lnfClassname, bean);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		IWidgetPropertyDescriptor property = propertyMap.get(id);
		if (property != null)
			property.setPropertyValue(bean, value);
	}
}

