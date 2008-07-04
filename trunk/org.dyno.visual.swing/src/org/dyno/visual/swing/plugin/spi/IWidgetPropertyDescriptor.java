/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.plugin.spi;

import javax.swing.JComponent;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
/**
 * 
 * IWidgetPropertyDescriptor
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public interface IWidgetPropertyDescriptor extends IPropertyDescriptor{
    boolean isPropertyResettable(Object bean);
    boolean isPropertySet(String lnfClassname, Object bean);
	Object getPropertyValue(Object bean);
	void resetPropertyValue(String lnfClassname, Object bean);
	void setPropertyValue(Object bean, Object value);
	boolean cloneProperty(Object bean, JComponent clone);
	String getSetCode(Object widget, ImportRewrite imports);
	boolean isGencode();
	void setBean(Object bean);
	void setFilterFlags(String[]filters);
	void setCategory(String categoryName);
	@SuppressWarnings("unchecked")
	void init(IConfigurationElement config, Class beanClass);
}
