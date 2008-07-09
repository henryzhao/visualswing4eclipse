/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.plugin.spi;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JComponent;

import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.base.PropertySource2;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
/**
 * 
 * LayoutAdapter
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public abstract class LayoutAdapter implements IPropertySourceProvider {
	public static String DEFAULT_LAYOUT = "java.awt.FlowLayout";
	public static final String LAYOUT_EXTENSION_POINT = "org.dyno.visual.swing.widgets.layoutAdapter";

	private static HashMap<String, IConfigurationElement> layoutAdapters;
	private static HashMap<String, ILayoutBean> layoutBeans;

	protected static final int THUMB_PAD = 24;
	protected static Color RED_COLOR = new Color(255, 164, 0);
	protected static Color GREEN_COLOR = new Color(164, 255, 0);
	protected static Stroke STROKE;

	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 2 }, 0);
		layoutAdapters = new HashMap<String, IConfigurationElement>();
		layoutBeans = new HashMap<String, ILayoutBean>();
		parseLayoutExtensions();
	}

	public static Set<String> getLayoutClasses() {
		return layoutAdapters.keySet();
	}

	public static IConfigurationElement getLayoutConfig(String layoutClass) {
		return layoutAdapters.get(layoutClass);
	}

	public static LayoutAdapter createLayoutAdapter(String layoutClass) {
		return createLayoutAdapter(getLayoutConfig(layoutClass));
	}

	@SuppressWarnings("unchecked")
	public static LayoutAdapter createLayoutAdapter(Class layoutClass) {
		return createLayoutAdapter(layoutClass.getName());
	}

	protected class Thumb {
		public int pi;
		public int x;
		public int y;

		public Thumb(int p, int x, int y) {
			this.pi = p;
			this.x = x;
			this.y = y;
		}

		public boolean equals(Object o) {
			if (o == null)
				return false;
			if (!(o instanceof Thumb))
				return false;
			Thumb another = (Thumb) o;
			return pi == another.pi;
		}
	}

	private static void parseLayoutExtensions() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(LAYOUT_EXTENSION_POINT);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseLayoutExtension(extensions[i]);
				}
			}
		}
	}

	private static void parseLayoutExtension(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("layout")) {
					addLayout(configs[i]);
				}
			}
		}
	}

	protected abstract LayoutManager copyLayout(Container con);

	private static void addLayout(IConfigurationElement config) {
		try {
			String layoutClass = config.getAttribute("layoutClass");
			String strDefault = config.getAttribute("default");
			if (strDefault != null && strDefault.toLowerCase().equals("true")) {
				DEFAULT_LAYOUT = layoutClass;
			}
			layoutAdapters.put(layoutClass, config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ILayoutBean getDefaultLayoutBean() {
		ILayoutBean bean = layoutBeans.get(DEFAULT_LAYOUT);
		if (bean == null) {
			IConfigurationElement config = layoutAdapters.get(DEFAULT_LAYOUT);
			if (config != null) {
				String strBean = config.getAttribute("layoutBean");
				if (strBean != null) {
					try {
						bean = (ILayoutBean) config.createExecutableExtension("layoutBean");
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return bean;
	}

	public static LayoutAdapter getLayoutAdapter(JComponent container) {
		LayoutManager layout = container.getLayout();
		Class<?> layoutClass = layout.getClass();
		while (layoutClass != null) {
			IConfigurationElement config = layoutAdapters.get(layoutClass.getName());
			if (config != null) {
				try {
					LayoutAdapter layoutAdapter = (LayoutAdapter) config.createExecutableExtension("class");
					layoutAdapter.setLayoutClass(layoutClass);
					String layoutName = config.getAttribute("name");
					if (layoutName != null && layoutName.trim().length() > 0)
						layoutAdapter.setName(layoutName);
					else
						layoutAdapter.setName(layoutClass.getName());
					return layoutAdapter;
				} catch (CoreException e) {
					e.printStackTrace();
					return null;
				}
			} else
				layoutClass = layoutClass.getSuperclass();
		}
		return null;
	}

	public static LayoutAdapter createLayoutAdapter(IConfigurationElement config) {
		try {
			LayoutAdapter layoutAdapter = (LayoutAdapter) config.createExecutableExtension("class");
			String layoutName = config.getAttribute("name");
			if (layoutName != null && layoutName.trim().length() > 0)
				layoutAdapter.setName(layoutName);
			return layoutAdapter;
		} catch (CoreException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String name;
	protected JComponent container;
	protected Class<?> layoutClass;

	private void setLayoutClass(Class<?> layoutClass) {
		this.layoutClass = layoutClass;
	}

	public Class<?> getLayoutClass() {
		return layoutClass;
	}

	public void setContainer(JComponent container) {
		this.container = container;
	}

	public void paintFocused(Graphics g) {
	}

	public boolean dragOver(Point p) {
		WidgetAdapter parent = WidgetAdapter.getWidgetAdapter(container);
		parent.setMascotLocation(p);
		return true;
	}

	public boolean drop(Point p) {
		return true;
	}

	public boolean dragEnter(Point p) {
		WidgetAdapter parent = WidgetAdapter.getWidgetAdapter(container);
		parent.setMascotLocation(p);
		return true;
	}

	public boolean dragExit(Point p) {
		return false;
	}

	public boolean isChildResizable() {
		return false;
	}

	public boolean removeChild(JComponent child) {
		container.remove(child);
		container.validate();
		return true;
	}

	public boolean isWidgetVisible(JComponent child) {
		return child.isVisible();
	}

	public void showChild(JComponent widget) {
		if (!widget.isVisible())
			widget.setVisible(true);
	}

	private void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract void initConainerLayout(Container container);

	public void addChild(JComponent widget) {
		container.add(widget);
	}

	public boolean doAlignment(String id) {
		return false;
	}

	public void doKeyPressed(KeyEvent e) {
	}

	public abstract boolean cloneLayout(JComponent container);

	public void adjustLayout(JComponent widget) {
	}

	public boolean isSelectionAlignResize(String id) {
		return false;
	}

	public void paintBaselineAnchor(Graphics g) {
	}

	public String createCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(container);
		if (adapter.isRoot())
			builder.append("setLayout(");
		else
			builder.append(getFieldName(adapter.getName()) + ".setLayout(");
		builder.append(getNewInstanceCode(imports));
		builder.append(");\n");
		CompositeAdapter conAdapter = (CompositeAdapter) adapter;
		int count = conAdapter.getChildCount();
		for (int i = 0; i < count; i++) {
			JComponent child = conAdapter.getChild(i);
			builder.append(getAddChildCode(child, imports));
		}
		return builder.toString();
	}

	protected abstract String getNewInstanceCode(ImportRewrite imports);

	protected String getAddChildCode(JComponent child, ImportRewrite imports) {
		String constraints = getChildConstraints(child, imports);
		StringBuilder builder = new StringBuilder();
		WidgetAdapter conAdapter = WidgetAdapter.getWidgetAdapter(container);
		WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
		if (conAdapter.isRoot()) {
			builder.append("add(");
		} else {
			builder.append(getFieldName(conAdapter.getName()) + ".add(");
		}
		builder.append(getGetMethodName(childAdapter.getName()) + "()");
		if (constraints != null) {
			builder.append(", " + constraints);
		}
		builder.append(");\n");
		return builder.toString();
	}

	protected String getGetMethodName(String name) {
		return NamespaceManager.getInstance().getGetMethodName(name);
	}

	protected String getFieldName(String name) {
		return NamespaceManager.getInstance().getFieldName(name);
	}

	protected String getChildConstraints(JComponent child, ImportRewrite imports) {
		return null;
	}

	public IPropertySource getPropertySource(Object layout) {		
		return new PropertySource2(layout, getLayoutProperties());
	}

	protected IWidgetPropertyDescriptor[] getLayoutProperties(){
		return new IWidgetPropertyDescriptor[0];
	}
}
