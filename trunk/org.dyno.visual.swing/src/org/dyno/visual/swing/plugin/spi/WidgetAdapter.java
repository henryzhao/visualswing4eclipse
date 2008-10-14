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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
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
import javax.swing.JRootPane;
import javax.swing.LookAndFeel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.WhiteBoard;
import org.dyno.visual.swing.adapter.BeanNameProperty;
import org.dyno.visual.swing.adapter.FieldAccessProperty;
import org.dyno.visual.swing.adapter.GetAccessProperty;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.base.PropertySource2;
import org.dyno.visual.swing.base.TypeAdapter;
import org.dyno.visual.swing.base.WidgetProperty;
import org.dyno.visual.swing.base.ExtensionRegistry.Category;
import org.dyno.visual.swing.base.ExtensionRegistry.Provider;
import org.dyno.visual.swing.base.ExtensionRegistry.Sorting;
import org.dyno.visual.swing.designer.GlassPlane;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.designer.WidgetSelection;
import org.dyno.visual.swing.editors.actions.AddEventAction;
import org.dyno.visual.swing.editors.actions.DelEventAction;
import org.dyno.visual.swing.editors.actions.LnfAction;
import org.dyno.visual.swing.editors.actions.VarChangeAction;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
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
public abstract class WidgetAdapter implements IExecutableExtension, Cloneable,
		IPropertySourceProvider, IConstants {
	protected boolean dirty;
	protected int getAccess;
	protected int fieldAccess;
	protected Point hotspotPoint;
	protected Component widget;
	protected String lastName;
	protected String name;
	protected String widgetName;
	protected boolean selected;
	protected HashMap<String, IConfigurationElement> propertyConfigs;
	protected Map<EventSetDescriptor, IEventListenerModel> eventDescriptor;
	protected Map<String, Boolean> edited;
	protected Image iconImage;

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

	public void validateContent() {
		VisualDesigner designer = getDesigner();
		if (designer != null)
			designer.validateContent();
	}

	public IUndoContext getUndoContext() {
		VisualDesigner designer = getDesigner();
		if (designer != null)
			return designer.getUndoContext();
		else
			return null;
	}

	public String getLastName() {
		return lastName;
	}

	public List<WidgetAdapter> getSelectedWidgets() {
		VisualDesigner designer = getDesigner();
		if (designer != null) {
			return designer.getSelectedWidgets();
		} else
			return null;
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
		if (getWidget() instanceof Container)
			layoutContainer((Container) getWidget());
	}

	protected WidgetAdapter() {
		this.widget = createWidget();
		this.hotspotPoint = new Point(widget.getWidth() / 2,
				widget.getHeight() / 2);
		attach();
		this.dirty = true;
		this.eventDescriptor = new HashMap<EventSetDescriptor, IEventListenerModel>();
		this.edited = new HashMap<String, Boolean>();
	}

	protected void attach() {
		if (widget instanceof JComponent) {
			((JComponent) widget).putClientProperty(ADAPTER_PROPERTY, this);
		} else if (widget instanceof RootPaneContainer) {
			JRootPane jrootPane = ((RootPaneContainer) widget).getRootPane();
			jrootPane.putClientProperty(ADAPTER_PROPERTY, this);
		}
	}

	protected WidgetAdapter(String name) {
		setName(name);
		this.widget = createWidget();
		this.hotspotPoint = new Point(widget.getWidth() / 2,
				widget.getHeight() / 2);
		attach();
		this.dirty = true;
		this.eventDescriptor = new HashMap<EventSetDescriptor, IEventListenerModel>();
		this.edited = new HashMap<String, Boolean>();
	}

	public Map<String, Boolean> getEdited() {
		return edited;
	}

	public void setWidget(Component widget) {
		this.widget = widget;
		attach();
		this.dirty = false;
	}

	public void detachWidget() {
		if (this.widget != null) {
			attach();
		}
	}

	protected abstract Component createWidget();

	public void setHotspotPoint(Point p) {
		this.hotspotPoint = p;
	}

	public Point getHotspotPoint() {
		return this.hotspotPoint;
	}

	public IEditor getEditorAt(int x, int y) {
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
		Component widget = getWidget();
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
			Component comp = focused.getWidget();
			if (comp instanceof Container) {
				LayoutManager layout = ((Container) comp).getLayout();
				if (layout == null)
					return true;
			}
			return focused.allowChildResize();
		}
	}

	public String getWidgetName() {
		return widgetName;
	}

	private Provider getProvider(HashMap<String, Provider> providers,
			Class<?> class1) {
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
		if (getWidget() instanceof JComponent)
			paintComponent(g, (JComponent) getWidget());
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

	public Component getWidget() {
		if (widget == null) {
			widget = createWidget();
			attach();
		}
		return widget;
	}

	public Component getRootPane() {
		return getWidget();
	}

	public Component getComponent() {
		return getWidget();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (this.name == null || !this.name.equals(name)) {
			if (this.name != null) {
				NamespaceManager.getInstance().removeName(this.name);
			}
			NamespaceManager.getInstance().addName(name);
			this.name = name;
		}
	}

	public void setSelected(boolean b) {
		selected = b;
		VisualDesigner designer = getDesigner();
		if (designer != null) {
			if (b)
				designer.addSelectedWidget(this);
			else
				designer.removeSelectedWidget(this);
		}
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

	public VisualDesigner getDesigner() {
		WidgetAdapter a = getRootAdapter();
		if (a != null) {
			return (VisualDesigner) a.getRootPane().getParent();
		} else
			return null;
	}

	public void repaintDesigner() {
		if (getDesigner() != null)
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

	public static WidgetAdapter getWidgetAdapter(Component comp) {
		if (comp instanceof JComponent)
			return (WidgetAdapter) ((JComponent) comp)
					.getClientProperty(ADAPTER_PROPERTY);
		else if (comp instanceof RootPaneContainer) {
			Container content = ((RootPaneContainer) comp).getRootPane();
			if (content instanceof JComponent) {
				return (WidgetAdapter) ((JComponent) content)
						.getClientProperty(ADAPTER_PROPERTY);
			}
		}
		return null;
	}

	public Border getDesignBorder() {
		return BorderFactory.createLineBorder(new Color(224, 224, 255), 4);
	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		widgetName = config.getAttribute("widgetName");
		String sIcon = config.getAttribute("icon");
		if (sIcon != null && sIcon.trim().length() > 0) {
			IContributor contributor = config.getContributor();
			String pluginId = contributor.getName();
			this.iconImage = VisualSwingPlugin.getSharedImage(pluginId, sIcon);
		}
		propertyConfigs = parseProperties(config); // ...
	}

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

	private HashMap<String, IConfigurationElement> parseProperties(
			IConfigurationElement config) {
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
				IConfigurationElement superConfig = ExtensionRegistry
						.getWidgetConfig(superClass);
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
		if (!isRoot()) {
			propdesc.add(new BeanNameProperty(this));
			propdesc.add(new FieldAccessProperty(this));
			propdesc.add(new GetAccessProperty(this));
		}
		IWidgetPropertyDescriptor[] properties = propdesc
				.toArray(new IWidgetPropertyDescriptor[propdesc.size()]);
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
					IConfigurationElement prop = this.propertyConfigs
							.get(refid);
					if (prop != null) {
						references.put(refid, refid);
						IWidgetPropertyDescriptor property = createProperty(
								prop, beanClass);
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
					IWidgetPropertyDescriptor property = createProperty(prop,
							beanClass);
					property.setCategory(category.getName());
					property.setFilterFlags(category.getFilters());
					propdesc.add(property);
				}
			}
		}
		if (!isRoot()) {
			CompositeAdapter parent = getParentAdapter();
			if (parent != null) {
				IWidgetPropertyDescriptor[] constraints = parent
						.getConstraintsProperties(getWidget());
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
	private IWidgetPropertyDescriptor createProperty(
			IConfigurationElement config, Class beanClass) {
		String sClass = config.getAttribute("class");
		if (sClass != null && sClass.trim().length() > 0) {
			IWidgetPropertyDescriptor iwpd;
			try {
				iwpd = (IWidgetPropertyDescriptor) config
						.createExecutableExtension("class");
				iwpd.init(config, beanClass);
				return iwpd;
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return new WidgetProperty(config, beanClass);
	}

	private void mergeProperties(
			HashMap<String, IConfigurationElement> eSources,
			HashMap<String, IConfigurationElement> eTargets) {
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
		Component me = getWidget();
		Container container = me.getParent();
		return container == null || container instanceof VisualDesigner;
	}

	public CompositeAdapter getParentAdapter() {
		if (isRoot())
			return null;
		Component me = getWidget();
		Component parent = me.getParent();
		while (parent != null) {
			if (parent instanceof Component) {
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(parent);
				if (adapter != null){
					if(adapter.getDelegateAdapter()!=null)
						return (CompositeAdapter)adapter.getDelegateAdapter();
					return (CompositeAdapter) adapter;
				}
			}
			parent = parent.getParent();
		}
		return null;
	}
	protected WidgetAdapter getDelegateAdapter(){
		return null;
	}
	public boolean isMoveable() {
		CompositeAdapter parentAdapter = (CompositeAdapter) getParentAdapter();
		if (parentAdapter != null) {
			Component comp = parentAdapter.getWidget();
			if (comp instanceof Container) {
				LayoutManager layoutMgr = ((Container) comp).getLayout();
				if (layoutMgr == null)
					return true;
			}
			return parentAdapter.isChildMoveable();
		} else {
			return false;
		}
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

	public boolean widgetPressed(MouseEvent e) {
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
					return ((CompositeAdapter) adapter)
							.isChildVisible(getWidget());
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
			menu.add(new VarChangeAction(this));
		else {
			MenuManager lnfMenu = new MenuManager("Set Look And Feel", "#LNF");
			fillLnfAction(lnfMenu);
			menu.add(lnfMenu);
		}
		MenuManager eventMenu = new MenuManager("Add/Edit Events", "#EVENT");
		fillAddEventAction(eventMenu);
		menu.add(eventMenu);
		MenuManager delEventMenu = new MenuManager("Delete Events",
				"#DELETE_EVENT");
		fillDelEventAction(delEventMenu);
		menu.add(delEventMenu);
		MenuManager borderMenu = new MenuManager("Border", "#BORDER");
		fillBorderAction(borderMenu);
		menu.add(borderMenu);
		if (!isRoot()) {
			CompositeAdapter parentAdapter = getParentAdapter();
			if (parentAdapter != null) {
				parentAdapter.fillConstraintsAction(menu, getWidget());
			}
		}
	}

	private void fillLnfAction(MenuManager lnfMenu) {
		LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
		for (LookAndFeelInfo info : infos) {
			IAction lnfAction = new LnfAction(this, info);
			lnfMenu.add(lnfAction);
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
			if (getWidget() instanceof JComponent) {
				IAction action = adapter
						.getContextAction((JComponent) getWidget());
				borderMenu.add(action);
			}
		}
	}

	protected void fillDelEventAction(MenuManager eventMenu) {
		Set<EventSetDescriptor> keys = eventDescriptor.keySet();
		for (EventSetDescriptor key : keys) {
			MenuManager subEventMenu = new MenuManager(key.getName(),
					"#DELETE_EVENT_" + key);
			IEventListenerModel model = eventDescriptor.get(key);
			Iterable<MethodDescriptor> mSet = model.methods();
			for (MethodDescriptor method : mSet) {
				subEventMenu.add(new DelEventAction(this, key, method));
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
			MenuManager subEventMenu = new MenuManager(esd.getName(),
					"#ADD_EVENT_" + esd.getName());
			MethodDescriptor[] eds = esd.getListenerMethodDescriptors();
			for (MethodDescriptor md : eds) {
				subEventMenu.add(new AddEventAction(this, esd, md));
			}
			eventMenu.add(subEventMenu);
		}
	}

	public Shell getShell() {
		return getDesigner().getShell();
	}

	public Object getBean() {
		return getWidget();
	}

	public Object clone() {
		return ExtensionRegistry.createAdapterFor(cloneWidget());
	}

	protected abstract Component newWidget();

	public Component cloneWidget() {
		Component clone = newWidget();
		ArrayList<IWidgetPropertyDescriptor> properties = getPropertyDescriptors();
		for (IWidgetPropertyDescriptor property : properties) {
			if (property.isPropertySet(getLnfClassname(), getWidget())) {
				property.cloneProperty(getWidget(), clone);
			}
		}
		return clone;
	}

	public Image getIconImage() {
		return iconImage;
	}

	public List<Component> getSelection() {
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

	public boolean genCode(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
		if (!dirty)
			return true;
		if (isRoot()) {
			return createRootCode(type, imports, monitor);
		} else {
			return createNonRootCode(type, imports, monitor);
		}
	}
	protected String getWidgetCodeClassName(){
		return getWidget().getClass().getName();
	}
	private boolean createNonRootCode(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
		boolean success = true;
		IJavaElement sibling = null;
		if (getLastName() != null) {
			IField lastField;
			if (!getLastName().equals(getName())) {
				lastField = type.getField(getFieldName(getLastName()));
			} else {
				lastField = type.getField(getFieldName(getName()));
			}
			if (lastField != null && lastField.exists()) {
				try {
					lastField.delete(true, monitor);
				} catch (Exception e) {
					success = false;
				}
			}
		}
		IField field = type.getField(getFieldName(getName()));
		if (field != null && !field.exists()) {
			StringBuilder builder = new StringBuilder();
			builder.append(getAccessCode(fieldAccess));
			builder.append(" ");
			String fqcn = getWidgetCodeClassName();
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
		sibling = null;
		if (getLastName() != null && !getLastName().equals(getName())) {
			String lastGetMethodName = getGetMethodName(getLastName());
			IMethod lastMethod = type.getMethod(lastGetMethodName,
					new String[0]);
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
		String fqcn = getWidgetCodeClassName();
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
			type.createMethod(JavaUtil.formatCode(builder.toString()), sibling,
					false, monitor);
		} catch (JavaModelException e) {
			success = false;
		}
		success = createEventMethod(type, imports, monitor);
		setLastName(getName());
		return success;
	}

	private boolean createEventMethod(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
		boolean success = true;
		Set<EventSetDescriptor> keySet = this.eventDescriptor.keySet();
		if (!keySet.isEmpty()) {
			for (EventSetDescriptor eventSet : keySet) {
				IEventListenerModel model = this.eventDescriptor.get(eventSet);
				success = model.createEventMethod(type, imports, monitor);
			}
		}
		return success;
	}

	private boolean createRootCode(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
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
		createPostInitCode(builder, imports);
		builder.append("}\n");
		try {
			type.createMethod(JavaUtil.formatCode(builder.toString()), sibling,
					false, monitor);
		} catch (JavaModelException e) {
			success = false;
		}
		success = createEventMethod(type, imports, monitor);
		success = createConstructor(type, imports, monitor);
		return success;
	}

	protected void createPostInitCode(StringBuilder builder,
			ImportRewrite imports) {
		Dimension size = getWidget().getSize();
		builder.append("setSize(" + size.width + ", " + size.height + ");\n");
	}

	protected boolean createConstructor(IType type, ImportRewrite imports,
			IProgressMonitor monitor) {
		return true;
	}

	protected String getGetMethodName(String name) {
		return NamespaceManager.getInstance().getGetMethodName(name);
	}

	protected String getFieldName(String lastName) {
		return NamespaceManager.getInstance().getFieldName(lastName);
	}

	protected String createGetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(getFieldName(getName()) + " = "
				+ getNewInstanceCode(imports) + ";\n");
		builder.append(createSetCode(imports));
		CompositeAdapter conAdapter = getParentAdapter();
		if (conAdapter.needGenBoundCode()) {
			Rectangle bounds = getWidget().getBounds();
			String strBounds = getFieldName(getName()) + ".setBounds("
					+ bounds.x + ", " + bounds.y + ", " + bounds.width + ", "
					+ bounds.height + ");\n";
			builder.append(strBounds);
		}
		builder.append(genAddEventCode(imports));
		return builder.toString();
	}

	private String createSetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		ArrayList<IWidgetPropertyDescriptor> properties = getPropertyDescriptors();
		for (IWidgetPropertyDescriptor property : properties) {
			if (property.isPropertySet(getLnfClassname(), getWidget())
					&& (property.isGencode() || property.isEdited(this))) {
				String setCode = property.getSetCode(getWidget(), imports);
				if (setCode != null)
					builder.append(setCode);
			}
		}
		return builder.toString();
	}

	private String genAddEventCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		Set<EventSetDescriptor> keySet = this.eventDescriptor.keySet();
		if (!keySet.isEmpty()) {
			for (EventSetDescriptor eventSet : keySet) {
				if (!isRoot())
					builder.append(getFieldName(getName()) + ".");
				Method mAdd = eventSet.getAddListenerMethod();
				builder.append(mAdd.getName() + "(");
				IEventListenerModel model = this.eventDescriptor.get(eventSet);
				String newcode = model.createListenerInstance(imports);
				builder.append(newcode);
				builder.append(");\n");
			}
		}
		return builder.toString();
	}

	protected String createInitCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(createSetCode(imports));
		String code = genAddEventCode(imports);
		builder.append(code);
		return builder.toString();
	}

	public void setFieldAccess(int fieldAccess) {
		this.fieldAccess = fieldAccess;
	}

	public int getFieldAccess() {
		return fieldAccess;
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

	protected String getNewInstanceCode(ImportRewrite imports) {
		String beanName = imports.addImport(getWidgetCodeClassName());
		return "new " + beanName + "()";
	}

	public Map<EventSetDescriptor, IEventListenerModel> getEventDescriptor() {
		return eventDescriptor;
	}

	@SuppressWarnings("unchecked")
	public boolean isWidgetValueChanged(Object newValue) {
		Object lastValue = getWidgetValue();
		if (lastValue == null) {
			if (newValue == null)
				return false;
			else
				return true;
		} else {
			if (newValue == null)
				return true;
			else {
				TypeAdapter typeAdapter = ExtensionRegistry
						.getTypeAdapter(lastValue.getClass());
				if (typeAdapter != null && typeAdapter.getComparator() != null)
					return typeAdapter.getComparator().compare(lastValue,
							newValue) != 0;
				return !lastValue.equals(newValue);
			}
		}
	}
}
