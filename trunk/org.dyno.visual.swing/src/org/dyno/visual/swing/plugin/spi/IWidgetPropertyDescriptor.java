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

package org.dyno.visual.swing.plugin.spi;

import java.awt.Component;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
/**
 * 
 * IWidgetPropertyDescriptor
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public interface IWidgetPropertyDescriptor extends IPropertyDescriptor, IAdaptable{
    boolean isPropertyResettable(IStructuredSelection bean);
    boolean isPropertySet(String lnfClassname, IStructuredSelection bean);
	Object getPropertyValue(IStructuredSelection bean);
	void resetPropertyValue(String lnfClassname, IStructuredSelection bean);
	void setPropertyValue(IStructuredSelection bean, Object value);
	boolean cloneProperty(Object bean, Component clone);
	String getSetCode(Object widget, ImportRewrite imports);
	boolean isGencode();
	void setBean(IStructuredSelection bean);
	void setFilterFlags(String[]filters);
	void setCategory(String categoryName);
	@SuppressWarnings("unchecked")
	void init(IConfigurationElement config, Class beanClass);
	boolean isEdited(WidgetAdapter adapter);
	IValueParser getValueParser();
	void setFieldValue(Object bean, Object newValue);
	Object getFieldValue(Object bean);
}

