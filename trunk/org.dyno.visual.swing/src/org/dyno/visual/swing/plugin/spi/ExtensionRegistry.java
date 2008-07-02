package org.dyno.visual.swing.plugin.spi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JComponent;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

public class ExtensionRegistry {
	private static final String ADAPTER_EXTENSION_POINT = "org.dyno.visual.swing.widgetAdapter";
	private static final String CATEGORY_EXTENSION_POINT = "org.dyno.visual.swing.propertyCategory";
	private static final String TYPE_EXTENSION_POINT = "org.dyno.visual.swing.valueType";
	private static final String LOOKANDFEEL_EXTENSION_POINT = "org.dyno.visual.swing.lnf.lnfAdapter";
	private static String CURRENT_SORTING;

	public static void setCurrentSorting(String currentSorting) {
		CURRENT_SORTING = currentSorting;
	}

	public static Sorting getCurrentSorting() {
		return propertySortings.get(CURRENT_SORTING);
	}

	public static WidgetAdapter createAdapterFor(JComponent comp) {
		WidgetAdapter adapter = createWidgetAdapter(comp.getClass());
		adapter.setWidget(comp);
		if (adapter instanceof CompositeAdapter) {
			CompositeAdapter compositeAdapter = (CompositeAdapter) adapter;
			int count = compositeAdapter.getChildCount();
			for (int i = 0; i < count; i++) {
				JComponent child = compositeAdapter.getChild(i);
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

	private static HashMap<String, IConfigurationElement> widgets;
	private static HashMap<String, Sorting> propertySortings;
	private static HashMap<String, TypeAdapter> typeAdapters;
	private static HashMap<String, ILookAndFeelAdapter> lnfAdapters;

	public static HashMap<String, IConfigurationElement> getRegisteredWidgets() {
		return widgets;
	}

	static {
		widgets = new HashMap<String, IConfigurationElement>();
		propertySortings = new HashMap<String, Sorting>();
		typeAdapters = new HashMap<String, TypeAdapter>();
		lnfAdapters = new HashMap<String, ILookAndFeelAdapter>();
		parseWidgetExtensions();
		parseSortingExtensions();
		parseTypeExtensions();
		parseLnfExtensions();
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
			e.printStackTrace();
		}
	}

	public static Set<String> getSortingKeys() {
		return propertySortings.keySet();
	}

	public static Sorting getSortingByKey(String id) {
		return propertySortings.get(id);
	}

	private static void addWidgetConfig(String widgetClass, IConfigurationElement config) {
		widgets.put(widgetClass, config);
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
			String sAttribute = config.getAttribute("endec");
			if (!isNull(sAttribute)) {
				adapter.setEndec((ICodeGen) config.createExecutableExtension("endec"));
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean isNull(String str) {
		return str == null || str.trim().length() == 0;
	}

	private static void addWidget(IConfigurationElement config) {
		String widgetClassname = config.getAttribute("widgetClass");
		addWidgetConfig(widgetClassname, config);
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
			initAdapter(conf, adapter);
			return adapter;
		} catch (CoreException e1) {
			e1.printStackTrace();
			return null;
		}
	}

	private static void initAdapter(IConfigurationElement conf, WidgetAdapter adapter) {
		String sSetborder = conf.getAttribute("setborder");
		boolean setborder = false;
		if (sSetborder != null && sSetborder.equals("true"))
			setborder = true;
		adapter.setSetBorder(setborder);
	}

	public static WidgetAdapter createWidgetAdapter(JComponent widget) {
		WidgetAdapter adapter = createWidgetAdapter(widget.getClass());
		adapter.setWidget(widget);
		return adapter;
	}

	public static WidgetAdapter createWidgetAdapter(String widgetClass) {
		try {
			IConfigurationElement conf = widgets.get(widgetClass);
			WidgetAdapter adapter = (WidgetAdapter) conf.createExecutableExtension("class");
			initAdapter(conf, adapter);
			return adapter;
		} catch (CoreException e1) {
			e1.printStackTrace();
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
