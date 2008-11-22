/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.plugin.spi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.border.Border;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.IFactory;
import org.dyno.visual.swing.base.ItemEndec;
import org.dyno.visual.swing.base.ItemProvider;
import org.dyno.visual.swing.base.PropertySource2;
import org.dyno.visual.swing.base.TypeAdapter;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
/**
 * 
 * BorderAdapter
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public abstract class BorderAdapter implements IFactory, IPropertySourceProvider, ICodeGen {
	public static final String BORDER_EXTENSION_POINT = "org.dyno.visual.swing.borders.borderAdapter";
	private static List<BorderAdapter> borderAdapters;
	@SuppressWarnings("unchecked")
	private static Map<Class, BorderAdapter> borderMap;
	static {
		initialize();
	}

	@SuppressWarnings("unchecked")
	private static void initialize() {
		borderAdapters = new ArrayList<BorderAdapter>();
		borderMap = new HashMap<Class, BorderAdapter>();
		parseBorderExtensions();
	}

	private static void parseBorderExtensions() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(BORDER_EXTENSION_POINT);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseBorderExtension(extensions[i]);
				}
			}
		}
	}

	private static void parseBorderExtension(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("border")) {
					addBorder(configs[i]);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void addBorder(IConfigurationElement config) {
		try {
			BorderAdapter adapter = (BorderAdapter) config.createExecutableExtension("adapter");
			borderAdapters.add(adapter);
			Class clazz = adapter.getBorderClass();
			if (clazz != null)
				borderMap.put(clazz, adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<BorderAdapter> getBorderList() {
		return borderAdapters;
	}

	@SuppressWarnings("unchecked")
	public static BorderAdapter getBorderAdapter(Class borderClass) {
		return borderMap.get(borderClass);
	}

	public IPropertySource getPropertySource(Object object) {
		if (object instanceof Border) {
			IWidgetPropertyDescriptor[] properties = getBorderProperties();
			if (properties != null)
				return new PropertySource2(object, properties);
		}
		return null;
	}

	protected IWidgetPropertyDescriptor[] getBorderProperties() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public abstract Class getBorderClass();

	public abstract String getBorderName();

	public abstract IAction getContextAction(JComponent widget);
	
	protected String encodeValue(Object value, ImportRewrite imports){
		if(value==null)
			return "null";
		TypeAdapter adapter = ExtensionRegistry.getTypeAdapter(value.getClass());
		if(adapter!=null&&adapter.getEndec()!=null)
			return adapter.getEndec().getJavaCode(value, imports);
		else
			return value.toString();
	}
	protected String encodeValue(ItemProvider provider, Object value, ImportRewrite imports){
		if(value==null)
			return "null";
		ItemEndec endec = new ItemEndec(provider);
		return endec.getJavaCode(value, imports);
	}
}
