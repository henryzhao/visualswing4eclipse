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

package org.dyno.visual.swing.editors;

import java.awt.Component;
import java.awt.LayoutManager;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.PropertySource2;
import org.dyno.visual.swing.designer.WidgetSelection;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
/**
 * 
 * WidgetAdapterContentProvider
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class WidgetAdapterContentProvider implements IPropertySourceProvider {
	private static IPropertySourceProvider getProvider(Object object) {
		if (object instanceof Component) {
			Component widget = (Component) object;
			return WidgetAdapter.getWidgetAdapter(widget);
		} else if (object instanceof LayoutManager) {
			LayoutAdapter adapter = LayoutAdapter.createLayoutAdapter(object.getClass());
			return adapter;
		} else if(object instanceof WidgetSelection){
			WidgetSelection selection = (WidgetSelection)object;
			if (!selection.isEmpty()) {
				if (selection.size() == 1) {
					return getProvider(selection.getFirstElement());
				} else {
					Object obj = selection.getFirstElement();
					Class clazz = obj.getClass();
					for (Object element : selection) {
						Class clazz2 = element.getClass();
						clazz = getCommonSuperClass(clazz, clazz2);
					}
					WidgetAdapter adapter = ExtensionRegistry
							.createWidgetAdapter(clazz);
					return adapter;
				}
			}
		}
		return null;
	}
	private static Class getCommonSuperClass(Class clazz1, Class clazz2){
		if(clazz1==Object.class)
			return clazz1;
		else if(clazz2==Object.class)
			return clazz2;
		else if(clazz1.isAssignableFrom(clazz2))
			return clazz1;
		else if(clazz2.isAssignableFrom(clazz1))
			return clazz2;
		else 
			return getCommonSuperClass(clazz1.getSuperclass(), clazz2.getSuperclass());
	}
	@Override
	public IPropertySource getPropertySource(Object object) {
		if (object == null)
			return new PropertySource2(null, new IWidgetPropertyDescriptor[0]);
		IPropertySourceProvider provider = getProvider(object);
		if (provider != null) {
			return provider.getPropertySource(object);
		} else
			return null;
	}
}

