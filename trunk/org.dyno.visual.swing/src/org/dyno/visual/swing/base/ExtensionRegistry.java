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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.dyno.visual.swing.plugin.spi.ICloner;
import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.dyno.visual.swing.plugin.spi.IContextCustomizer;
import org.dyno.visual.swing.plugin.spi.IEndec;
import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.dyno.visual.swing.plugin.spi.ILibraryExtension;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;
import org.dyno.visual.swing.plugin.spi.IValueParser;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
/**
 * 
 * ExtensionRegistry
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class ExtensionRegistry {
	private static final String ADAPTER_EXTENSION_POINT = "org.dyno.visual.swing.widgetAdapter";
	private static final String CATEGORY_EXTENSION_POINT = "org.dyno.visual.swing.propertyCategory";
	private static final String TYPE_EXTENSION_POINT = "org.dyno.visual.swing.valueType";
	private static final String LOOKANDFEEL_EXTENSION_POINT = "org.dyno.visual.swing.lnf.lnfAdapter";
	private static final String CUSTOMIZED_CONTEXT_MENU_EXTENSION_POINT="org.dyno.visual.swing.contextCustomizer";
	private static final String LIBRARY_EXTENSION_POINT="org.dyno.visual.swing.libraryExtension";
	private static final String ADAPTABLE_ADAPTER_EXTENSION_POINT="org.dyno.visual.swing.adapters";
	private static final String INVISIBLE_ADPTER_EXTENSION_POINT="org.dyno.visual.swing.invisibleAdapter";
	private static String CURRENT_SORTING;

	public static void setCurrentSorting(String currentSorting) {
		CURRENT_SORTING = currentSorting;
	}

	public static Sorting getCurrentSorting() {
		return propertySortings.get(CURRENT_SORTING);
	}

	public static WidgetAdapter createAdapterFor(Component comp) {
		WidgetAdapter adapter = createWidgetAdapter(comp.getClass());
		adapter.setWidget(comp);
		if (adapter instanceof CompositeAdapter) {
			CompositeAdapter compositeAdapter = (CompositeAdapter) adapter;
			int count = compositeAdapter.getChildCount();
			for (int i = 0; i < count; i++) {
				Component child = compositeAdapter.getChild(i);
				createAdapterFor(child);
			}
		}
		return adapter;
	}

	public static class Sorting {
		private String id;
		private String name;
		private String default_category;
		private boolean hideGroup;
		private HashMap<String, Category> categories;

		public Sorting(String id, String name) {
			this.id = id;
			this.name = name;
			categories = new HashMap<String, Category>();
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public HashMap<String, Category> getCategories() {
			return categories;
		}

		public String getDefaultCategory() {
			return default_category;
		}

		public void setDefaultCategory(String def_category) {
			this.default_category = def_category;
		}

		public boolean isHideGroup() {
			return hideGroup;
		}

		public void setHideGroup(boolean hideGroup) {
			this.hideGroup = hideGroup;
		}

	}

	public static class Category {
		private int order;
		private String id;
		private String name;
		private HashMap<String, Provider> providers;
		private List<String> filters;

		public Category(int o, String id, String name) {
			this.order = o;
			this.id = id;
			this.name = name;
			filters = new ArrayList<String>();
			this.providers = new HashMap<String, Provider>();
		}

		public String[] getFilters() {
			return filters.isEmpty() ? null : filters.toArray(new String[filters.size()]);
		}

		public int getOrder() {
			return order;
		}

		public void addFilter(String filter) {
			filters.add(filter);
		}

		public String getName() {
			return name;
		}

		public String getId() {
			return id;
		}

		public HashMap<String, Provider> getProviders() {
			return providers;
		}
	}

	public static class Provider {
		private String classname;
		private ArrayList<String> refids;

		public Provider(String classname) {
			this.classname = classname;
			this.refids = new ArrayList<String>();
		}

		public ArrayList<String> getRefIds() {
			return refids;
		}

		public String getClassname() {
			return classname;
		}
	}

	private static Map<String, IConfigurationElement> widgets;
	private static Map<String, Map<String, IConfigurationElement>> adapters;
	private static Map<String, Sorting> propertySortings;
	private static Map<String, TypeAdapter> typeAdapters;
	private static Map<String, ILookAndFeelAdapter> lnfAdapters;
	private static Map<String, IConfigurationElement> invisibles;
	private static List<IContextCustomizer> contextCustomizers;
	private static List<ILibraryExtension> libExtensions;
	public static IConfigurationElement getInvisibleConfig(String className){
		return invisibles.get(className);
	}
	public static List<ILibraryExtension> getLibExtensions(){
		return libExtensions;
	}
	public static List<IContextCustomizer> getContextCustomizers(){
		return contextCustomizers;
	}
	public static Map<String, IConfigurationElement> getRegisteredWidgets() {
		return widgets;
	}

	static {
		widgets = new HashMap<String, IConfigurationElement>();
		invisibles = new HashMap<String, IConfigurationElement>();
		propertySortings = new HashMap<String, Sorting>();
		typeAdapters = new HashMap<String, TypeAdapter>();
		lnfAdapters = new HashMap<String, ILookAndFeelAdapter>();
		contextCustomizers = new ArrayList<IContextCustomizer>();
		libExtensions= new ArrayList<ILibraryExtension>();
		adapters = new HashMap<String, Map<String, IConfigurationElement>>();
		parseWidgetExtensions();
		parseSortingExtensions();
		parseTypeExtensions();
		parseLnfExtensions();
		parseLibExtensions();
		parseContextMenus();
		parseAdapters();
		parseInvisibleAdapters();
	}
	public static void registerLnfAdapter(String classname, ILookAndFeelAdapter lnfAdapter){
		lnfAdapters.put(classname, lnfAdapter);
	}

	private static void parseInvisibleAdapters() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(INVISIBLE_ADPTER_EXTENSION_POINT);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseInvisibleExtension(extensions[i]);
				}
			}
		}		
	}



	public static Collection<ILookAndFeelAdapter> getLnfAdapters(){
		return lnfAdapters.values();
	}
	public static ILookAndFeelAdapter getLnfAdapter(String lnfClassname) {
		return lnfAdapters.get(lnfClassname);
	}


	private static void parseLnfExtensions() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(LOOKANDFEEL_EXTENSION_POINT);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseLnfExtension(extensions[i]);
				}
			}
		}
	}

	private static void parseLnfExtension(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("lookandfeel")) {
					addLnf(configs[i]);
				}
			}
		}
	}

	private static void addLnf(IConfigurationElement config) {
		try {
			String sClass = config.getAttribute("class");
			ILookAndFeelAdapter adapter = (ILookAndFeelAdapter) config.createExecutableExtension("adapter");
			lnfAdapters.put(sClass, adapter);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
	}

	public static Set<String> getSortingKeys() {
		return propertySortings.keySet();
	}

	public static Sorting getSortingByKey(String id) {
		return propertySortings.get(id);
	}

	private static void parseTypeExtensions() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(TYPE_EXTENSION_POINT);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseTypeExtension(extensions[i]);
				}
			}
		}
	}

	private static void parseContextMenus() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(CUSTOMIZED_CONTEXT_MENU_EXTENSION_POINT);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseContextExtension(extensions[i]);
				}
			}
		}
	}


	private static void parseLibExtensions() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(LIBRARY_EXTENSION_POINT);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseLibExtension(extensions[i]);
				}
			}
		}
	}	

	private static void parseWidgetExtensions() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(ADAPTER_EXTENSION_POINT);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseWidgetExtension(extensions[i]);
				}
			}
		}
	}
	private static void parseAdapters() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(ADAPTABLE_ADAPTER_EXTENSION_POINT);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseAdapterExtension(extensions[i]);
				}
			}
		}
	}
	public static IConfigurationElement getAdapterConfig(String interfaceClassname, String adapterClassname){
		Map<String, IConfigurationElement> entry=adapters.get(interfaceClassname);
		if(entry==null)
			return null;
		return entry.get(adapterClassname);
	}

	private static void parseSortingExtensions() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(CATEGORY_EXTENSION_POINT);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseSortingExtension(extensions[i]);
				}
			}
		}
	}

	private static void parseTypeExtension(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("type")) {
					addType(configs[i]);
				}
			}
		}
	}
	private static void parseContextExtension(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("context")) {
					try {
						contextCustomizers.add((IContextCustomizer) configs[i].createExecutableExtension("class"));
					} catch (CoreException e) {
						VisualSwingPlugin.getLogger().error(e);
					}
				}
			}
		}
	}
	private static void parseLibExtension(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("library")) {
					try {
						libExtensions.add((ILibraryExtension) configs[i].createExecutableExtension("class"));
					} catch (CoreException e) {
						VisualSwingPlugin.getLogger().error(e);
					}
				}
			}
		}
	}
	
	private static void parseWidgetExtension(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("widget")) {
					addWidget(configs[i]);
				}
			}
		}
	}
	private static void parseInvisibleExtension(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("adapter")) {
					addInvisibleAdapter(configs[i]);
				}
			}
		}
	}
	private static void addInvisibleAdapter(IConfigurationElement config) {
		String type = config.getAttribute("type");
		invisibles.put(type, config); 
	}

	private static void parseAdapterExtension(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("interface")) {
					String interfaceClassname = configs[i].getAttribute("class");
					addInterface(interfaceClassname, configs[i]);
				}
			}
		}
	}

	private static void parseSortingExtension(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				parseSorting(configs[i]);
			}
		}
	}

	private static void parseSorting(IConfigurationElement config) {
		String name = config.getName();
		if (name.equals("sorting")) {
			String id = config.getAttribute("id");
			String sortingName = config.getAttribute("name");
			String def = config.getAttribute("default");
			if (def != null && def.equals("true"))
				CURRENT_SORTING = id;
			Sorting sorting = new Sorting(id, sortingName);
			String shg = config.getAttribute("hideGroup");
			if (shg != null && shg.equals("true"))
				sorting.setHideGroup(true);
			propertySortings.put(id, sorting);
			parseCategoryExtension(config, sorting);
		}
	}

	private static void parseCategoryExtension(IConfigurationElement config, Sorting sorting) {
		IConfigurationElement[] configs = config.getChildren();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("category")) {
					String id = configs[i].getAttribute("id");
					String categoryname = configs[i].getAttribute("name");
					String def = configs[i].getAttribute("default");
					if (def != null && def.equals("true"))
						sorting.setDefaultCategory(id);
					Category category = new Category(i, id, categoryname);
					sorting.getCategories().put(id, category);
					String filters = configs[i].getAttribute("filters");
					if (filters != null && filters.trim().length() > 0) {
						StringTokenizer tokenizer = new StringTokenizer(filters, ",");
						while (tokenizer.hasMoreTokens()) {
							String token = tokenizer.nextToken().trim();
							category.addFilter(token);
						}
					}
					parseProviderExtension(configs[i], category);
				}
			}
		}
	}

	private static void parseProviderExtension(IConfigurationElement config, Category category) {
		IConfigurationElement[] configs = config.getChildren();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("provider")) {
					String classname = configs[i].getAttribute("class");
					Provider provider = new Provider(classname);
					category.getProviders().put(classname, provider);
					parseReferenceExtension(configs[i], provider);
				}
			}
		}
	}

	private static void parseReferenceExtension(IConfigurationElement config, Provider provider) {
		IConfigurationElement[] configs = config.getChildren();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("reference")) {
					String id = configs[i].getAttribute("id");
					provider.getRefIds().add(id);
				}
			}
		}
	}

	private static void addType(IConfigurationElement config) {
		String typeClassname = config.getAttribute("class");
		TypeAdapter adapter = getTypeAdapter(typeClassname);
		if (adapter == null) {
			adapter = new TypeAdapter();
			typeAdapters.put(typeClassname, adapter);
		}
		try {
			String sAttribute = config.getAttribute("codegen");
			if (!isNull(sAttribute)) {
				adapter.setCodegen((ICodeGen) config.createExecutableExtension("codegen"));
			}
			sAttribute = config.getAttribute("comparator");
			if (!isNull(sAttribute)) {
				adapter.setComparator((Comparator<?>) config.createExecutableExtension("comparator"));
			}
			sAttribute = config.getAttribute("renderer");
			if (!isNull(sAttribute)) {
				adapter.setRenderer((ILabelProviderFactory) config.createExecutableExtension("renderer"));
			}
			sAttribute = config.getAttribute("editor");
			if (!isNull(sAttribute)) {
				adapter.setEditor((ICellEditorFactory) config.createExecutableExtension("editor"));
			}
			sAttribute = config.getAttribute("cloner");
			if (!isNull(sAttribute)) {
				adapter.setCloner((ICloner) config.createExecutableExtension("cloner"));
			}
			sAttribute = config.getAttribute("parser");
			if(!isNull(sAttribute)){
				adapter.setParser((IValueParser)config.createExecutableExtension("parser"));
			}
			sAttribute = config.getAttribute("endec");
			if(!isNull(sAttribute)){
				adapter.setEndec((IEndec)config.createExecutableExtension("endec"));
			}
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
	}

	private static boolean isNull(String str) {
		return str == null || str.trim().length() == 0;
	}

	private static void addWidget(IConfigurationElement config) {
		String widgetClassname = config.getAttribute("widgetClass");
		widgets.put(widgetClassname, config);
	}
	private static void addInterface(String interfaceClassname, IConfigurationElement configurationElement) {
		Map<String, IConfigurationElement> interfaceEntry=adapters.get(interfaceClassname);
		if(interfaceEntry==null){
			interfaceEntry = new HashMap<String, IConfigurationElement>();
			adapters.put(interfaceClassname, interfaceEntry);
		}
		IConfigurationElement[] configs = configurationElement.getChildren();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("adapter")) {
					String adapterClassname = configs[i].getAttribute("class");
					interfaceEntry.put(adapterClassname, configs[i]);
				}
			}
		}
	}

	public static IConfigurationElement getWidgetConfig(String widgetClass) {
		return widgets.get(widgetClass);
	}

	public static IConfigurationElement getWidgetConfig(Class<?> widgetClass) {
		IConfigurationElement element = widgets.get(widgetClass.getName());
		if (element != null)
			return element;
		Class<?> superclass = widgetClass.getSuperclass();
		if (superclass != null)
			return getWidgetConfig(superclass);
		else
			return null;
	}

	public static WidgetAdapter createWidgetAdapter(Class<?> widgetClass) {
		try {
			IConfigurationElement conf = getWidgetConfig(widgetClass);
			WidgetAdapter adapter = (WidgetAdapter) conf.createExecutableExtension("class");
			return adapter;
		} catch (CoreException e) {
			VisualSwingPlugin.getLogger().error(e);
			return null;
		}
	}

	public static WidgetAdapter createWidgetAdapter(Component widget) {
		WidgetAdapter adapter = createWidgetAdapter(widget.getClass());
		adapter.setWidget(widget);
		return adapter;
	}

	public static WidgetAdapter createWidgetAdapter(String widgetClass) {
		try {
			IConfigurationElement conf = widgets.get(widgetClass);
			WidgetAdapter adapter = (WidgetAdapter) conf.createExecutableExtension("class");
			return adapter;
		} catch (CoreException e) {
			VisualSwingPlugin.getLogger().error(e);
			return null;
		}
	}

	public static TypeAdapter getTypeAdapter(Class<?> clazz) {
		if (clazz.isInterface()) {
			TypeAdapter adapter = getTypeAdapter(clazz.getName());
			if (adapter != null)
				return adapter;
			Class<?>[] interfs = clazz.getInterfaces();
			if (interfs != null) {
				for (Class<?> interf : interfs) {
					adapter = getTypeAdapter(interf);
					if (adapter != null)
						return adapter;
				}
			}
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && superClass != Object.class) {
				adapter = getTypeAdapter(superClass);
				if (adapter != null)
					return adapter;
			}
			return null;
		} else {
			TypeAdapter adapter = getTypeAdapter(clazz.getName());
			if (adapter != null)
				return adapter;
			Class<?>[] interfs = clazz.getInterfaces();
			if (interfs != null) {
				for (Class<?> interf : interfs) {
					adapter = getTypeAdapter(interf);
					if (adapter != null)
						return adapter;
				}
			}
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null) {
				adapter = getTypeAdapter(superClass);
				if (adapter != null)
					return adapter;
			}
			return null;
		}
	}

	public static TypeAdapter getTypeAdapter(String clazz) {
		return typeAdapters.get(clazz);
	}
}

