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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JComponent;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.PropertySource2;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
/**
 * 
 * LayoutAdapter
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
@SuppressWarnings("unchecked")
public abstract class LayoutAdapter extends AbstractAdaptable implements IPropertySourceProvider, ILayoutBean, IAdapter {
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

	@Override
	public void requestNewName() {
	}
	public String getID(){
			return name;
	}
	@Override
	public String getBasename() {
		Class<?> lClass = getLayoutClass();
		if(lClass!=null){
			String className = lClass.getName();
			int dot = className.lastIndexOf('.');
			if (dot != -1)
				className = className.substring(dot + 1);
			return Character.toLowerCase(className.charAt(0)) + className.substring(1);
		}else
			return null;
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
			VisualSwingPlugin.getLogger().error(e);
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
						VisualSwingPlugin.getLogger().error(e);
					}
				}
			}
		}
		return bean;
	}

	public static LayoutAdapter getLayoutAdapter(Container container) {
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
					VisualSwingPlugin.getLogger().error(e);
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
			VisualSwingPlugin.getLogger().error(e);
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
	public JComponent getContainer(){
		return this.container;
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

	public boolean removeChild(Component child) {
		container.remove(child);
		container.validate();
		return true;
	}

	public boolean isWidgetVisible(Component child) {
		return child.isVisible();
	}

	public void showChild(Component widget) {
		if (!widget.isVisible())
			widget.setVisible(true);
	}

	private void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public void addChild(Component widget) {
		container.add(widget);
	}

	public boolean doAlignment(String id) {
		return false;
	}

	public IUndoableOperation doKeyPressed(KeyEvent e) {
		return null;
	}

	public abstract boolean cloneLayout(JComponent container);

	public void adjustLayout(Component widget) {
	}

	public boolean isSelectionAlignResize(String id) {
		return false;
	}
	public void paintHint(Graphics g) {
	}
	public void paintHovered(Graphics g) {
	}
	public void paintGrid(Graphics clipg) {
	}
	public void paintAnchor(Graphics g) {
	}	
	public IPropertySource getPropertySource(Object layout) {		
		return new PropertySource2(new StructuredSelection(layout), getLayoutProperties());
	}

	protected IWidgetPropertyDescriptor[] getLayoutProperties(){
		return new IWidgetPropertyDescriptor[0];
	}

	public void addAfter(Component hovering, Component dragged) {
	}

	public void addBefore(Component hovering, Component dragged) {
	}

	public abstract void addChildByConstraints(Component child, Object constraints);

	public abstract Object getChildConstraints(Component child);

	public void fillConstraintsAction(MenuManager menu, Component child) {
	}

	@Override
	public Image getIconImage() {
		return null;
	}

	@Override
	protected Class getObjectClass() {
		return getLayoutClass();
	}

	@Override
	public IAdapter getParent() {
		return null;
	}

	public boolean isDefaultLayout() {
		CompositeAdapter ca = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		return ca.getDefaultLayout()==getLayoutClass();
	}

}

