/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.editors;

import java.awt.Component;
import java.awt.LayoutManager;

import org.dyno.visual.swing.base.PropertySource2;
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
		}  
		return null;
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
