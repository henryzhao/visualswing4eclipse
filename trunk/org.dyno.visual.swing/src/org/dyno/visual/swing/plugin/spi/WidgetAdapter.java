/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.plugin.spi;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.designer.Event;
import org.dyno.visual.swing.designer.GlassPlane;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.designer.WidgetSelection;
import org.dyno.visual.swing.plugin.spi.ExtensionRegistry.Category;
import org.dyno.visual.swing.plugin.spi.ExtensionRegistry.Provider;
import org.dyno.visual.swing.plugin.spi.ExtensionRegistry.Sorting;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.osgi.framework.Bundle;

/**
 * 
 * WidgetAdapter
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public abstract class WidgetAdapter implements IExecutableExtension, Cloneable, IPropertySourceProvider {
	public static final String ADAPTER_PROPERTY = "widget.adapter";
	public static final String LISTENER_ADAPTER_EXTENSION = "org.dyno.visual.swing.listenerAdapter";
	public static final int OUTER = 0;
	public static final int INNER = 1;
	public static final int LEFT_TOP = 2;
	public static final int TOP = 3;
	public static final int RIGHT_TOP = 4;
	public static final int RIGHT = 5;
	public static final int RIGHT_BOTTOM = 6;
	public static final int BOTTOM = 7;
	public static final int LEFT_BOTTOM = 8;
	public static final int LEFT = 9;
	public static final int ADHERE_PAD = 6;

	public static final int ACCESS_PRIVATE = 0;
	public static final int ACCESS_DEFAULT = 1;
	public static final int ACCESS_PROTECTED = 2;
	public static final int ACCESS_PUBLIC = 3;

	private static HashMap<String, String> used_names = new HashMap<String, String>();
	@SuppressWarnings("unchecked")
	private static HashMap<Class, Class> listenerAdapters;
	private static Color SELECTION_COLOR = new Color(255, 164, 0);
	static {
		initListenerAdapters();
	}

	@SuppressWarnings("unchecked")
	private static void initListenerAdapters() {
		listenerAdapters = new HashMap<Class, Class>();
		parseListenerAdapterExtensions();
		listenerAdapters.put(MouseListener.class, MouseAdapter.class);
	}
	@SuppressWarnings("unchecked")
	public static Class getListenerAdapter(Class list){
		return listenerAdapters.get(list);
	}
	private static void parseListenerAdapterExtensions() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(LISTENER_ADAPTER_EXTENSION);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseListenerAdapterExtension(extensions[i]);
				}
			}
		}
	}

	private static void parseListenerAdapterExtension(IExtension extension) {
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("listener")) {
					addListenerAdapter(configs[i]);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void addListenerAdapter(IConfigurationElement config) {
		String interf = config.getAttribute("interface");
		String adapter = config.getAttribute("adapter");
		IContributor contrib = config.getContributor();
		String pluginID = contrib.getName();
		Bundle bundle = Platform.getBundle(pluginID);
		try {
			Class interClass = bundle.loadClass(interf);
			Class adapterClass = bundle.loadClass(adapter);
			listenerAdapters.put(interClass, adapterClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected boolean dirty;
	protected int getAccess;
	protected int fieldAccess;
	protected Point hotspotPoint;
	protected JComponent widget;
	protected String lastName;
	protected String name;
	protected String widgetName;
	protected boolean selected;
	protected HashMap<String, IConfigurationElement> propertyConfigs;
	protected Map<EventSetDescriptor, Map<MethodDescriptor, String>> eventDescriptor;
	protected void layoutContainer(Container container) {
		container.doLayout();
		int count = container.getComponentCount();
		for (int i = 0; i < count; i++) {
			Component child = container.getComponent(i);
			if (child instanceof Container) {
				layoutContainer((Container) child);
			}
		}
	}

	protected String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setDirty(boolean d) {
		if (dirty != d) {
			dirty = d;
			if (getDesigner() != null)
				getDesigner().fireDirty();
		}
	}

	public void clearDirty() {
		dirty = false;
	}

	public int getGetAccess() {
		return getAccess;
	}

	public void setGetAccess(int access) {
		this.getAccess = access;
	}

	public boolean isDirty() {
		return dirty;
	}

	public String toString() {
		if (isRoot()) {
			return "[" + getWidgetName() + "]";
		} else {
			return getName() + " [" + getWidgetName() + "]";
		}
	}

	protected boolean editValue() {
		return getDesigner().editComponent(getWidget());
	}

	public void doLayout() {
		layoutContainer(getWidget());
	}

	protected WidgetAdapter() {
		this.widget = createWidget();
		this.hotspotPoint = new Point(widget.getWidth() / 2, widget.getHeight() / 2);
		this.widget.putClientProperty(ADAPTER_PROPERTY, this);
		this.dirty = true;
		this.eventDescriptor = new HashMap<EventSetDescriptor, Map<MethodDescriptor, String>>();
	}

	protected WidgetAdapter(String name) {
		setName(name);
		this.widget = createWidget();
		this.hotspotPoint = new Point(widget.getWidth() / 2, widget.getHeight() / 2);
		this.widget.putClientProperty(ADAPTER_PROPERTY, this);
		this.dirty = true;
		this.eventDescriptor = new HashMap<EventSetDescriptor, Map<MethodDescriptor, String>>();
	}

	public void setWidget(JComponent widget) {
		this.widget = widget;
		this.widget.putClientProperty(ADAPTER_PROPERTY, this);
		this.dirty = false;
	}

	public void detachWidget() {
		if (this.widget != null) {
			this.widget.putClientProperty(ADAPTER_PROPERTY, null);
		}
	}

	protected abstract JComponent createWidget();

	public void setHotspotPoint(Point p) {
		this.hotspotPoint = p;
	}

	public Point getHotspotPoint() {
		return this.hotspotPoint;
	}

	public Editor getEditorAt(int x, int y) {
		return null;
	}

	public void setWidgetValue(Object value) {
	}

	public Object getWidgetValue() {
		return null;
	}

	public Rectangle getEditorBounds(int x, int y) {
		return null;
	}

	public int getCursorLocation(Point p) {
		JComponent widget = getWidget();
		int w = widget.getWidth();
		int h = widget.getHeight();
		int x = p.x;
		int y = p.y;
		if (x < -ADHERE_PAD) {
			return OUTER;
		} else if (x < ADHERE_PAD) {
			if (y < -ADHERE_PAD) {
				return OUTER;
			} else if (y < ADHERE_PAD) {
				return LEFT_TOP;
			} else if (y < h - ADHERE_PAD) {
				return LEFT;
			} else if (y < h + ADHERE_PAD) {
				return LEFT_BOTTOM;
			} else {
				return OUTER;
			}
		} else if (x < w - ADHERE_PAD) {
			if (y < -ADHERE_PAD) {
				return OUTER;
			} else if (y < ADHERE_PAD) {
				return TOP;
			} else if (y < h - ADHERE_PAD) {
				return INNER;
			} else if (y < h + ADHERE_PAD) {
				return BOTTOM;
			} else {
				return OUTER;
			}
		} else if (x < w + ADHERE_PAD) {
			if (y < -ADHERE_PAD) {
				return OUTER;
			} else if (y < ADHERE_PAD) {
				return RIGHT_TOP;
			} else if (y < h - ADHERE_PAD) {
				return RIGHT;
			} else if (y < h + ADHERE_PAD) {
				return RIGHT_BOTTOM;
			} else {
				return OUTER;
			}
		} else {
			return OUTER;
		}
	}

	public boolean isResizable() {
		if (isRoot())
			return false;
		CompositeAdapter focused = getCurrentFocused();
		if (focused == null)
			return false;
		else {
			LayoutManager layout = focused.getWidget().getLayout();
			if (layout == null)
				return true;
			return focused.allowChildResize();
		}
	}

	public String getWidgetName() {
		return widgetName;
	}

	private Provider getProvider(HashMap<String, Provider> providers, Class<?> class1) {
		String classname = class1.getName();
		Provider provider = providers.get(classname);
		if (provider == null && class1 != Component.class) {
			Class<?> superClass = class1.getSuperclass();
			if (superClass == null)
				return null;
			return getProvider(providers, superClass);
		} else
			return provider;
	}

	public void paintMascot(Graphics g) {
		paintComponent(g, getWidget());
	}

	protected void paintComponent(Graphics g, JComponent root) {
		int w = root.getWidth();
		int h = root.getHeight();
		ArrayList<Component> comps = new ArrayList<Component>();
		unsetDB(comps, root);
		Graphics clipg = g.create(1, 1, w, h);
		getComponent().paint(clipg);
		clipg.dispose();
		setDB(comps);
		Color old = g.getColor();
		g.setColor(SELECTION_COLOR);
		g.drawRect(0, 0, w + 1, h + 1);
		g.setColor(old);
	}

	private void setDB(ArrayList<Component> db) {
		for (Component comp : db) {
			if (comp instanceof JComponent) {
				((JComponent) comp).setDoubleBuffered(true);
			}
		}
	}

	private void unsetDB(ArrayList<Component> db, Container container) {

		if (container instanceof JComponent && container.isDoubleBuffered()) {
			((JComponent) container).setDoubleBuffered(false);
			db.add(container);
		}
		int count = container.getComponentCount();
		for (int i = 0; i < count; i++) {
			Component component = container.getComponent(i);
			if (component instanceof Container)
				unsetDB(db, (Container) component);
		}
	}

	public JComponent getWidget() {
		if (widget == null) {
			widget = createWidget();
			widget.putClientProperty(ADAPTER_PROPERTY, this);
		}
		return widget;
	}

	public JComponent getComponent() {
		return getWidget();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (this.name == null || !this.name.equals(name)) {
			if (this.name != null)
				used_names.remove(this.name);
			used_names.put(name, name);
			this.name = name;
		}
	}

	public void setSelected(boolean b) {
		selected = b;
	}

	public void changeNotify() {
		VisualDesigner d = getDesigner();
		if (d != null) {
			d.publishSelection();
		}
	}

	public void addNotify() {
		VisualDesigner d = getDesigner();
		if (d != null) {
			d.publishSelection();
		}
	}

	protected VisualDesigner getDesigner() {
		WidgetAdapter a = getRootAdapter();
		if (a != null) {
			return (VisualDesigner) a.getWidget().getParent();
		} else
			return null;
	}

	protected void repaintDesigner() {
		getDesigner().repaint();
	}

	protected GlassPlane getGlass() {
		VisualDesigner d = getDesigner();
		if (d != null) {
			return d.getGlass();
		} else
			return null;
	}

	public boolean isSelected() {
		return selected;
	}

	public void clearSelection() {
		setSelected(false);
	}

	public static WidgetAdapter getWidgetAdapter(JComponent comp) {
		return (WidgetAdapter) comp.getClientProperty(ADAPTER_PROPERTY);
	}

	public Border getDesignBorder() {
		return BorderFactory.createLineBorder(new Color(224, 224, 255), 4);
	}

	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		widgetName = config.getAttribute("widgetName");
		String sIcon = config.getAttribute("icon");
		if (sIcon != null && sIcon.trim().length() > 0) {
			IContributor contributor = config.getContributor();
			String pluginId = contributor.getName();
			this.iconImage = VisualSwingPlugin.getSharedImage(pluginId, sIcon);
		}
		propertyConfigs = parseProperties(config); // ...
	}

	private Image iconImage;

	@SuppressWarnings("unchecked")
	private Class getWidgetClass(IConfigurationElement config) {
		try {
			String widgetClassname = config.getAttribute("widgetClass");
			IContributor contributor = config.getContributor();
			String pluginId = contributor.getName();
			Bundle bundle = Platform.getBundle(pluginId);
			return bundle.loadClass(widgetClassname);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private HashMap<String, IConfigurationElement> parseProperties(IConfigurationElement config) {
		try {
			@SuppressWarnings("unchecked")
			Class widgetClass = getWidgetClass(config);
			HashMap<String, IConfigurationElement> eProperties = new HashMap<String, IConfigurationElement>();
			IConfigurationElement[] props = config.getChildren("property");
			for (IConfigurationElement prop : props) {
				String propertyId = prop.getAttribute("id");
				eProperties.put(propertyId, prop);
			}
			if (widgetClass != Component.class) {
				@SuppressWarnings("unchecked")
				Class superClass = widgetClass.getSuperclass();
				IConfigurationElement superConfig = ExtensionRegistry.getWidgetConfig(superClass);
				while (superConfig == null && superClass != Component.class) {
					superClass = superClass.getSuperclass();
					superConfig = ExtensionRegistry.getWidgetConfig(superClass);
				}
				if (superConfig != null) {
					HashMap<String, IConfigurationElement> eSuperProperties = parseProperties(superConfig);
					mergeProperties(eProperties, eSuperProperties);
				}
			}
			return eProperties;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public IPropertySource getPropertySource(Object object) {
		ArrayList<IWidgetPropertyDescriptor> propdesc = getPropertyDescriptors();
		IWidgetPropertyDescriptor[] properties = propdesc.toArray(new IWidgetPropertyDescriptor[propdesc.size()]);
		return new PropertySource2(getLnfClassname(), getWidget(), properties);
	}

	public String getLnfClassname() {
		VisualDesigner designer = getDesigner();
		if (designer != null)
			return designer.getLnfClassname();
		else {
			LookAndFeel lnf = UIManager.getLookAndFeel();
			String lnfClass;
			if (lnf == null)
				lnfClass = UIManager.getCrossPlatformLookAndFeelClassName();
			else
				lnfClass = lnf.getClass().getName();
			return lnfClass;
		}
	}

	@SuppressWarnings("unchecked")
	private ArrayList<IWidgetPropertyDescriptor> getPropertyDescriptors() {
		Sorting sorting = ExtensionRegistry.getCurrentSorting();
		HashMap<String, String> references = new HashMap<String, String>();
		Class beanClass = getWidget().getClass();
		ArrayList<IWidgetPropertyDescriptor> propdesc = new ArrayList<IWidgetPropertyDescriptor>();
		for (Category category : sorting.getCategories().values()) {
			Provider provider = getProvider(category.getProviders(), beanClass);
			if (provider != null) {
				for (String refid : provider.getRefIds()) {
					IConfigurationElement prop = this.propertyConfigs.get(refid);
					if (prop != null) {
						references.put(refid, refid);
						IWidgetPropertyDescriptor property = createProperty(prop, beanClass);
						property.setCategory(category.getName());
						property.setFilterFlags(category.getFilters());
						propdesc.add(property);
					}
				}
			}
		}
		String defaultId = sorting.getDefaultCategory();
		Category category = sorting.getCategories().get(defaultId);
		for (String refid : propertyConfigs.keySet()) {
			if (references.get(refid) == null) {
				references.put(refid, refid);
				IConfigurationElement prop = propertyConfigs.get(refid);
				if (prop != null) {
					references.put(refid, refid);
					IWidgetPropertyDescriptor property = createProperty(prop, beanClass);
					property.setCategory(category.getName());
					property.setFilterFlags(category.getFilters());
					propdesc.add(property);
				}
			}
		}
		if (!isRoot()) {
			CompositeAdapter parent = getParentAdapter();
			if (parent != null) {
				IWidgetPropertyDescriptor[] constraints = parent.getConstraintsProperties(getWidget());
				if (constraints != null) {
					for (IWidgetPropertyDescriptor prop : constraints) {
						propdesc.add(prop);
					}
				}
			}
		}
		return propdesc;
	}

	@SuppressWarnings("unchecked")
	private IWidgetPropertyDescriptor createProperty(IConfigurationElement config, Class beanClass) {
		String sClass = config.getAttribute("class");
		if (sClass != null && sClass.trim().length() > 0) {
			IWidgetPropertyDescriptor iwpd;
			try {
				iwpd = (IWidgetPropertyDescriptor) config.createExecutableExtension("class");
				iwpd.init(config, beanClass);
				return iwpd;
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return new WidgetProperty(config, beanClass);
	}

	private void mergeProperties(HashMap<String, IConfigurationElement> eSources, HashMap<String, IConfigurationElement> eTargets) {
		for (IConfigurationElement eTarget : eTargets.values()) {
			String eId = eTarget.getAttribute("id");
			if (eSources.get(eId) == null) {
				eSources.put(eId, eTarget);
			}
		}
	}

	public WidgetAdapter getRootAdapter() {
		if (isRoot())
			return this;
		WidgetAdapter parent = getParentAdapter();
		if (parent == null)
			return null;
		return parent.getRootAdapter();
	}

	public boolean isRoot() {
		JComponent me = getWidget();
		Container container = me.getParent();
		return container instanceof VisualDesigner;
	}

	public CompositeAdapter getParentAdapter() {
		if (isRoot())
			return null;
		JComponent me = getWidget();
		Component parent = me.getParent();
		while (parent != null) {
			if (parent instanceof JComponent) {
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter((JComponent) parent);
				if (adapter != null)
					return (CompositeAdapter) adapter;
			}
			parent = parent.getParent();
		}
		return null;
	}

	public boolean isMoveable() {
		CompositeAdapter parentAdapter = (CompositeAdapter) getParentAdapter();
		LayoutManager layoutMgr = parentAdapter.getWidget().getLayout();
		if (layoutMgr == null)
			return true;
		return parentAdapter.isChildMoveable();
	}

	protected CompositeAdapter getCurrentFocused() {
		VisualDesigner designer = getDesigner();
		if (designer != null) {
			return designer.getFocused();
		} else
			return null;
	}

	public void setMascotLocation(Point p) {
		GlassPlane glassPlane = getGlass();
		if (glassPlane != null) {
			glassPlane.setHotspotPoint(convertToGlobal(p));
		}
	}

	public Point getMascotLocation() {
		GlassPlane glassPlane = getGlass();
		Point p = glassPlane.getHotspotPoint();
		if (glassPlane != null && p != null) {
			return convertToLocal(glassPlane.getHotspotPoint());
		} else {
			return null;
		}
	}

	public WidgetAdapter getDropWidget() {
		return WhiteBoard.getSelectedWidget();
	}

	protected Point getGlassHotspot() {
		GlassPlane glassPlane = getGlass();
		if (glassPlane != null)
			return glassPlane.getHotspotPoint();
		else
			return null;
	}

	public Point convertToLocal(Point p) {
		VisualDesigner designer = getDesigner();
		if (designer != null) {
			return SwingUtilities.convertPoint(designer, p, getWidget());
		} else
			return p;
	}

	public Point convertToGlobal(Point p) {
		VisualDesigner designer = getDesigner();
		if (designer != null) {
			return SwingUtilities.convertPoint(getWidget(), p, designer);
		} else
			return p;
	}

	public void clearAllSelected() {
		VisualDesigner designer = getDesigner();
		if (designer != null)
			designer.clearSelection();
	}

	public int getState() {
		GlassPlane glass = getGlass();
		if (glass != null)
			return glass.getState();
		else
			return 0;
	}

	public boolean dragExit(Point p) {
		return false;
	}

	public boolean dragOver(Point p) {
		return false;
	}

	public boolean drop(Point p) {
		return false;
	}

	public boolean dragEnter(Point p) {
		return false;
	}

	public void paintFocused(Graphics clipg) {
	}

	public int getBaseline() {
		return getWidget().getHeight() / 2;
	}

	public int getBaseline(int h) {
		return h / 2;
	}

	public int getHeightByBaseline(int baseline) {
		return 2 * baseline;
	}

	public int getHeightByDescent(int descent) {
		return 2 * descent;
	}

	public boolean widgetPressed(int x, int y) {
		return true;
	}

	public boolean isVisible() {
		boolean visible = getWidget().isVisible();
		if (visible) {
			if (isRoot())
				return true;
			else {
				WidgetAdapter adapter = getParentAdapter();
				if (adapter.isVisible()) {
					return ((CompositeAdapter) adapter).isChildVisible(getWidget());
				} else
					return false;
			}
		} else
			return false;
	}

	public void makeVisibleInTree() {
		if (!isVisible() && !isRoot()) {
			CompositeAdapter parent = (CompositeAdapter) getParentAdapter();
			parent.makeVisibleInTree();
			parent.showChild(getWidget());
		}
	}

	public void fillContextAction(MenuManager menu) {
		if (!isRoot())
			menu.add(new VarChangeAction());
		else {
			MenuManager lnfMenu = new MenuManager("Set Look And Feel", "#LNF");
			fillLnfAction(lnfMenu);
			menu.add(lnfMenu);
		}
		MenuManager eventMenu = new MenuManager("Add/Edit Events", "#EVENT");
		fillAddEventAction(eventMenu);
		menu.add(eventMenu);
		MenuManager delEventMenu = new MenuManager("Delete Events", "#DELETE_EVENT");
		fillDelEventAction(delEventMenu);
		menu.add(delEventMenu);
		if (canSetBorder()) {
			MenuManager borderMenu = new MenuManager("Border", "#BORDER");
			fillBorderAction(borderMenu);
			menu.add(borderMenu);
		}

	}

	private boolean setBorder = false;

	void setSetBorder(boolean sb) {
		setBorder = sb;
	}

	protected boolean canSetBorder() {
		return setBorder;
	}

	private void fillLnfAction(MenuManager lnfMenu) {
		LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
		for (LookAndFeelInfo info : infos) {
			IAction lnfAction = new LnfAction(info);
			lnfMenu.add(lnfAction);
		}
	}

	class LnfAction extends Action {
		private LookAndFeelInfo info;

		public LnfAction(LookAndFeelInfo info) {
			super(info.getName(), AS_RADIO_BUTTON);
			this.info = info;
			String lnf = getLnfClassname();
			setChecked(lnf != null && lnf.equals(info.getClassName()));
		}

		@Override
		public void run() {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					changeLnf();
				}
			});
		}

		private void changeLnf() {
			String lnf = getLnfClassname();
			if (lnf != null && !lnf.getClass().getName().equals(info.getClassName())) {
				try {
					setLnfClassname(info.getClassName());
					SwingUtilities.updateComponentTreeUI(getDesigner());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setLnfClassname(String lnfClassname) {
		VisualDesigner designer = getDesigner();
		if (designer != null) {
			designer.setLnfClassname(lnfClassname);
		}
	}

	private void fillBorderAction(MenuManager borderMenu) {
		List<BorderAdapter> list = BorderAdapter.getBorderList();

		for (BorderAdapter adapter : list) {
			IAction action = adapter.getContextAction(getWidget());
			borderMenu.add(action);
		}
	}

	protected void fillDelEventAction(MenuManager eventMenu) {
		Set<EventSetDescriptor> keys = eventDescriptor.keySet();
		for (EventSetDescriptor key : keys) {
			MenuManager subEventMenu = new MenuManager(key.getName(), "#DELETE_EVENT_" + key);
			Map<MethodDescriptor, String> methods = eventDescriptor.get(key);
			Set<MethodDescriptor> mSet = methods.keySet();
			for (MethodDescriptor method : mSet) {
				String mName = methods.get(method);
				subEventMenu.add(new DelEventAction(key, method, mName));
			}
			eventMenu.add(subEventMenu);
		}
	}

	public BeanInfo getBeanInfo() {
		try {
			Class<?> clazz = getWidget().getClass();
			return Introspector.getBeanInfo(clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void fillAddEventAction(MenuManager eventMenu) {
		EventSetDescriptor[] esds = getBeanInfo().getEventSetDescriptors();
		for (EventSetDescriptor esd : esds) {
			MenuManager subEventMenu = new MenuManager(esd.getName(), "#ADD_EVENT_" + esd.getName());
			MethodDescriptor[] eds = esd.getListenerMethodDescriptors();
			for (MethodDescriptor md : eds) {
				subEventMenu.add(new AddEventAction(esd, md));
			}
			eventMenu.add(subEventMenu);
		}
	}

	class DelEventAction extends Action {
		private EventSetDescriptor eventSet;
		private MethodDescriptor methodDesc;

		public DelEventAction(EventSetDescriptor eventSet, MethodDescriptor methodDesc, String mName) {
			super(methodDesc.getDisplayName() + "  [" + mName + "]");
			setId(eventSet.getName() + "_" + methodDesc.getName());
			this.eventSet = eventSet;
			this.methodDesc = methodDesc;
			Map<MethodDescriptor, String> map = eventDescriptor.get(eventSet);
			String content = map.get(methodDesc);
			if(content.startsWith("method[")&&content.endsWith("]")){
				mName = content.substring("method[".length(), content.length()-1);
				setText(mName);
			}else{
				setText(methodDesc.getDisplayName());
			}			
		}

		public void run() {
			Map<MethodDescriptor, String> map = eventDescriptor.get(eventSet);
			if (map != null) {
				if (map.containsKey(methodDesc)) {
					map.remove(methodDesc);
				}
				if (map.isEmpty()) {
					eventDescriptor.remove(eventSet);
				}
				WidgetAdapter.this.changeNotify();
				WidgetAdapter.this.setDirty(true);
			}
		}
	}

	private String getCapitalName(String name) {
		return NamespaceManager.getInstance().getCapitalName(name);
	}

	class AddEventAction extends Action {
		private EventSetDescriptor eventSet;
		private MethodDescriptor methodDesc;

		public AddEventAction(EventSetDescriptor eventSet, MethodDescriptor methodDesc) {
			super(methodDesc.getDisplayName(), SWT.CHECK);
			this.eventSet = eventSet;
			this.methodDesc = methodDesc;
			setId(eventSet.getName() + "_" + methodDesc.getName());
			Map<MethodDescriptor, String> map = eventDescriptor.get(eventSet);
			if (map != null) {
				if (map.containsKey(methodDesc)) {
					String content = map.get(methodDesc);
					String mName;
					if(content.startsWith("method[")&&content.endsWith("]")){
						mName = content.substring("method[".length(), content.length()-1);
						setText(mName);
					}else{
						setText(methodDesc.getDisplayName());
					}			
					setChecked(true);
				}
			}
		}

		public void run() {
			Map<MethodDescriptor, String> map = eventDescriptor.get(eventSet);
			if (map == null) {
				map = new HashMap<MethodDescriptor, String>();
				eventDescriptor.put(eventSet, map);
			}
			if (!map.containsKey(methodDesc)) {
				String content = "method[";
				if (isRoot())
					content += eventSet.getName();
				else
					content += getName() + getCapitalName(eventSet.getName());
				content += getCapitalName(methodDesc.getName()) + "]";
				map.put(methodDesc, content);
			}
			WidgetAdapter.this.setDirty(true);
			WidgetAdapter.this.changeNotify();
			String content = map.get(methodDesc);
			if (content.startsWith("method[") && content.endsWith("]")) {
				String methodName = content.substring("method[".length(), content.length() - 1);
				Class<?>[] pd = methodDesc.getMethod().getParameterTypes();
				if (pd.length > 0) {
					String pname = pd[0].getName();
					int dot = pname.lastIndexOf('.');
					if (dot != -1)
						pname = pname.substring(dot + 1);
					String typeSig = Signature.createTypeSignature(pname, false);
					WhiteBoard.sendEvent(new Event(this, Event.EVENT_SHOW_SOURCE, new Object[] { WidgetAdapter.this, true, methodName, typeSig }));
				}
			} else if (content.startsWith("code[") && content.endsWith("]")) {
				WhiteBoard.sendEvent(new Event(this, Event.EVENT_SHOW_SOURCE, new Object[] { WidgetAdapter.this, false, eventSet, methodDesc }));
			}
		}
	}

	protected Shell getShell() {
		return getDesigner().getShell();
	}

	class VarChangeAction extends Action {
		public VarChangeAction() {
			setText("Change Variable Name ...");
			setId("change_variable_name");
			setToolTipText("Change Variable Name ...");
		}

		public void run() {
			while (true) {
				VarNameDialog dialog = new VarNameDialog(getShell());
				dialog.setPromptMessage("Please enter a new variable name for this component:");
				dialog.setInput(getName());
				if (dialog.open() == Dialog.OK) {
					try {
						String name = dialog.getInput();
						validateName(name);
						setName(name);
						if (!isRoot()) {
							getParentAdapter().setDirty(true);
						}
						setDirty(true);
						changeNotify();
						break;
					} catch (Exception e) {
						MessageDialog.openError(getShell(), "Invalid identifier", e.getMessage());
					}
				} else
					break;
			}
		}
	}

	public void validateName(String newName) throws Exception {
		if (newName == null || newName.trim().length() == 0)
			throw new Exception("Please specify non-empty name!");
		char ch = newName.charAt(0);
		if (!Character.isJavaIdentifierStart(ch)) {
			throw new Exception("Illegal variable name!");
		}
		int index = 1;
		while (index < newName.length()) {
			ch = newName.charAt(index++);
			if (!Character.isJavaIdentifierPart(ch))
				throw new Exception("Illegal variable name!");
		}
		if (!newName.equals(getName()) && used_names.get(newName) != null)
			throw new Exception("Already used variable name!");
	}

	public Object getBean() {
		return getWidget();
	}

	public Object clone() {
		return ExtensionRegistry.createAdapterFor(cloneWidget());
	}

	public JComponent cloneWidget() {
		JComponent clone = createWidget();
		ArrayList<IWidgetPropertyDescriptor> properties = getPropertyDescriptors();
		for (IWidgetPropertyDescriptor property : properties) {
			if (property.isPropertySet(getLnfClassname(), getWidget())) {
				property.cloneProperty(getWidget(), clone);
			}
		}
		return clone;
	}

	public boolean canImport(List<WidgetAdapter> srcAdapters) {
		for (WidgetAdapter adapter : srcAdapters) {
			if (!canImport(adapter))
				return false;
		}
		return true;
	}

	public boolean canImport(WidgetAdapter srcAdapter) {
		JComponent src = srcAdapter.getComponent();
		JComponent target = getWidget();
		if (SwingUtilities.isDescendingFrom(src, target))
			return true;
		if (SwingUtilities.isDescendingFrom(target, src))
			return false;
		if (this instanceof CompositeAdapter) {
			if (((CompositeAdapter) this).canAcceptMoreComponent())
				return true;
		}
		return false;
	}

	public Image getIconImage() {
		return iconImage;
	}

	public List<JComponent> getSelection() {
		return new WidgetSelection(getRootAdapter().getWidget());
	}

	public void paintBaselineAnchor(Graphics g) {
	}

	public Rectangle getDesignBounds() {
		int w = getWidget().getWidth();
		if (w <= 0)
			w = 400;
		int h = getWidget().getHeight();
		if (h <= 0)
			h = 300;
		return new Rectangle(24, 24, w, h);
	}

	private static CodeFormatter codeFormatter;

	@SuppressWarnings("unchecked")
	public static CodeFormatter getCodeFormatter() {
		if (codeFormatter == null) {
			Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
			options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_5);
			options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_5);
			options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5);
			options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ENUM_CONSTANTS, DefaultCodeFormatterConstants.createAlignmentValue(true,
					DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
			options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "160");
			codeFormatter = ToolFactory.createCodeFormatter(options);
		}
		return codeFormatter;
	}

	public static String formatCode(String source) {
		TextEdit edit = getCodeFormatter().format(CodeFormatter.K_UNKNOWN, source, 0, source.length(), 0, System.getProperty("line.separator"));
		if (edit != null) {
			IDocument document = new Document(source);
			try {
				edit.apply(document);
				return document.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return source;
	}

	protected IJavaElement getSibling(IType type, IJavaElement element) {
		try {
			IJavaElement[] children = type.getChildren();
			for (int i = 0; i < children.length; i++) {
				if (children[i].equals(element) && i < children.length - 1) {
					return children[i + 1];
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}

	private IJavaElement getInitMethodSibling(IType type) {
		String initMethodName = "initComponent";
		IMethod method = type.getMethod(initMethodName, new String[0]);
		if (method != null && method.exists()) {
			return getSibling(type, method);
		}
		return null;
	}

	public boolean genCode(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		if (!dirty)
			return true;
		if (isRoot()) {
			return createRootCode(type, imports, monitor);
		} else {
			return createNonRootCode(type, imports, monitor);
		}
	}

	private boolean createNonRootCode(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		boolean success = true;
		IJavaElement sibling = null;
		if (getLastName() != null && !getLastName().equals(getName())) {
			IField lastField = type.getField(getFieldName(getLastName()));
			if (lastField != null && lastField.exists()) {
				try {
					lastField.rename(getName(), true, monitor);
				} catch (Exception e) {
					success = false;
				}
			}
		} else {
			IField field = type.getField(getFieldName(getName()));
			if (field != null && !field.exists()) {
				StringBuilder builder = new StringBuilder();
				builder.append(getAccessCode(fieldAccess));
				builder.append(" ");
				String fqcn = getWidget().getClass().getName();
				String beanName = imports.addImport(fqcn);
				builder.append(beanName);
				builder.append(" ");
				builder.append(getFieldName(getName()));
				builder.append(";\n");
				try {
					type.createField(builder.toString(), sibling, false, monitor);
				} catch (JavaModelException e) {
					success = false;
				}
			}
		}
		sibling = null;
		if (getLastName() != null && !getLastName().equals(getName())) {
			String lastGetMethodName = getGetMethodName(getLastName());
			IMethod lastMethod = type.getMethod(lastGetMethodName, new String[0]);
			if (lastMethod != null && lastMethod.exists()) {
				try {
					sibling = getSibling(type, lastMethod);
					lastMethod.delete(true, monitor);
				} catch (Exception e) {
					success = false;
				}
			}
		}
		StringBuilder builder = new StringBuilder();
		String getMethodName = getGetMethodName(getName());
		IMethod method = type.getMethod(getMethodName, new String[0]);
		if (method != null && method.exists()) {
			try {
				sibling = getSibling(type, method);
				method.delete(false, monitor);
			} catch (JavaModelException e) {
				success = false;
			}
		}
		builder.append(getAccessCode(getAccess));
		builder.append(" ");
		String fqcn = getWidget().getClass().getName();
		String beanName = imports.addImport(fqcn);
		builder.append(beanName);
		builder.append(" ");
		builder.append(getMethodName);
		builder.append("(){\n");
		builder.append("if(");
		builder.append(getFieldName(getName()));
		builder.append("==null){\n");
		builder.append(createGetCode(imports));
		builder.append("}\n");
		builder.append("return ");
		builder.append(getFieldName(getName()));
		builder.append(";\n");
		builder.append("}\n");
		try {
			if (sibling == null)
				sibling = getInitMethodSibling(type);
			type.createMethod(formatCode(builder.toString()), sibling, false, monitor);
		} catch (JavaModelException e) {
			success = false;
		}
		success = createEventMethod(type, imports, monitor);
		setLastName(getName());
		return success;
	}

	@SuppressWarnings("unchecked")
	private boolean createEventMethod(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		boolean success = true;
		Set<EventSetDescriptor> keySet = this.eventDescriptor.keySet();
		if (!keySet.isEmpty()) {
			for (EventSetDescriptor eventSet : keySet) {
				Map<MethodDescriptor, String> map = this.eventDescriptor.get(eventSet);
				Set<MethodDescriptor> mSet = map.keySet();
				for (MethodDescriptor mdesc : mSet) {
					Method mEvent = mdesc.getMethod();
					if (getLastName() == null || isRoot() || getName().equals(getLastName())) {
						StringBuilder builder = new StringBuilder(0);
						String content = map.get(mdesc);
						if (content.startsWith("method[") && content.endsWith("]")) {
							String mName = content.substring("method[".length(), content.length() - 1);
							Class[] pTypes = mEvent.getParameterTypes();
							String pcName = pTypes[0].getName();
							pcName = imports.addImport(pcName);
							String[] paras = new String[] { Signature.createTypeSignature(pcName, false) };
							IMethod eventMethod = type.getMethod(mName, paras);
							if (!eventMethod.exists()) {
								builder.append("private void ");
								builder.append(mName + "(");
								builder.append(pcName);
								builder.append(" event");
								builder.append("){\n");
								builder.append("}\n");
								try {
									type.createMethod(formatCode(builder.toString()), null, false, monitor);
								} catch (JavaModelException e) {
									success = false;
								}
							}
						}
					}
				}
			}
		}
		return success;
	}

	private boolean createRootCode(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		boolean success = true;
		String initMethodName = "initComponent";
		IMethod method = type.getMethod(initMethodName, new String[0]);
		IJavaElement sibling = null;
		if (method != null && method.exists()) {
			try {
				sibling = getSibling(type, method);
				method.delete(false, monitor);
			} catch (JavaModelException e) {
				success = false;
			}
		}
		StringBuilder builder = new StringBuilder();
		builder.append("private void ");
		builder.append(initMethodName);
		builder.append("(){\n");
		builder.append(createInitCode(imports));
		int w = getWidget().getWidth();
		int h = getWidget().getHeight();
		builder.append("setSize(" + w + ", " + h + ");\n");
		builder.append("}\n");
		try {
			type.createMethod(formatCode(builder.toString()), sibling, false, monitor);
		} catch (JavaModelException e) {
			success = false;
		}
		success = createEventMethod(type, imports, monitor);
		return success;
	}

	protected String getGetMethodName(String name) {
		return NamespaceManager.getInstance().getGetMethodName(name);
	}

	protected String getFieldName(String lastName) {
		return NamespaceManager.getInstance().getFieldName(lastName);
	}

	protected String createGetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(getFieldName(getName()) + " = " + getNewInstanceCode(imports) + ";\n");
		createSetCode(imports, builder);
		CompositeAdapter conAdapter = getParentAdapter();
		if (conAdapter.needGenBoundCode()) {
			Rectangle bounds = getWidget().getBounds();
			String strBounds = getFieldName(getName()) + ".setBounds(" + bounds.x + ", " + bounds.y + ", " + bounds.width + ", " + bounds.height + ");\n";
			builder.append(strBounds);
		}
		genAddEventCode(imports, builder);
		return builder.toString();
	}

	private void createSetCode(ImportRewrite imports, StringBuilder builder) {
		ArrayList<IWidgetPropertyDescriptor> properties = getPropertyDescriptors();
		for (IWidgetPropertyDescriptor property : properties) {
			if (property.isGencode() && property.isPropertySet(getLnfClassname(), getWidget())) {
				String setCode = property.getSetCode(getWidget(), imports);
				if (setCode != null)
					builder.append(setCode);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void genAddEventCode(ImportRewrite imports, StringBuilder builder) {
		Set<EventSetDescriptor> keySet = this.eventDescriptor.keySet();
		if (!keySet.isEmpty()) {
			for (EventSetDescriptor eventSet : keySet) {
				if (!isRoot())
					builder.append(getFieldName(getName()) + ".");
				Method mAdd = eventSet.getAddListenerMethod();
				builder.append(mAdd.getName() + "(new ");
				Class lClass = eventSet.getListenerType();
				boolean hasAdapter = listenerAdapters.get(lClass) != null;
				if (hasAdapter)
					lClass = listenerAdapters.get(lClass);
				String lName = lClass.getName();
				String cName = imports.addImport(lName);
				builder.append(cName + "(){\n");
				Map<MethodDescriptor, String> map = this.eventDescriptor.get(eventSet);
				Set<MethodDescriptor> mKey = map.keySet();
				for (MethodDescriptor mdesc : mKey) {
					Method mEvent = mdesc.getMethod();
					boolean genOverride = !hasAdapter || map.containsKey(mdesc);
					if (genOverride) {
						builder.append("@Override\n");
						builder.append("public void " + mEvent.getName() + "(");
						Class[] pTypes = mEvent.getParameterTypes();
						if (pTypes != null && pTypes.length > 0) {
							String pcName = pTypes[0].getName();
							pcName = imports.addImport(pcName);
							builder.append(pcName);
							builder.append(" event");
						}
						builder.append("){\n");
					}
					if (map.containsKey(mdesc)) {
						String content = map.get(mdesc);
						if (content.startsWith("method[") && content.endsWith("]")) {
							String mName = content.substring("method[".length(), content.length()-1);
							builder.append(mName + "(event);\n");
						}else if(content.startsWith("code[")&&content.endsWith("]")){
							String mCode = content.substring("code[".length(), content.length()-1);
							builder.append(mCode);
						}
					}
					if (genOverride)
						builder.append("}\n");
				}
				builder.append("});\n");
			}
		}
	}

	protected String createInitCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		createSetCode(imports, builder);
		genAddEventCode(imports, builder);
		return builder.toString();
	}

	public int getFieldAccess() {
		return fieldAccess;
	}

	public void setFieldAccess(int fieldAccess) {
		this.fieldAccess = fieldAccess;
	}

	protected String getAccessCode(int access) {
		switch (access) {
		case ACCESS_PRIVATE:
			return "private";
		case ACCESS_PROTECTED:
			return "protected";
		case ACCESS_PUBLIC:
			return "public";
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	protected String getNewInstanceCode(ImportRewrite imports) {
		Class beanClass = getWidget().getClass();
		String beanName = imports.addImport(beanClass.getName());
		return "new " + beanName + "()";
	}

	public Map<EventSetDescriptor, Map<MethodDescriptor, String>> getEventDescriptor() {
		return eventDescriptor;
	}

	public void setEventDescriptor(Map<EventSetDescriptor, Map<MethodDescriptor, String>> eventDescriptor) {
		this.eventDescriptor = eventDescriptor;
	}
}
