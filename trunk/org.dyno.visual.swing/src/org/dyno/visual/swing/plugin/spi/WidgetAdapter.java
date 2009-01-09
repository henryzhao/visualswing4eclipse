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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.LookAndFeel;
import javax.swing.MenuElement;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.WhiteBoard;
import org.dyno.visual.swing.adapter.BeanNameProperty;
import org.dyno.visual.swing.adapter.FieldAccessProperty;
import org.dyno.visual.swing.adapter.GetAccessProperty;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.MenuSelectionManager;
import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.base.NamespaceUtil;
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
import org.dyno.visual.swing.editors.actions.VarChangeAction;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.StructuredSelection;
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
public abstract class WidgetAdapter extends AbstractAdaptable implements
		IExecutableExtension, Cloneable, IPropertySourceProvider, IConstants,
		IAdapter {
	private static Icon FORBIDDEN_ICON;
	static {
		FORBIDDEN_ICON = new ImageIcon(WidgetAdapter.class
				.getResource("/icons/forbidden.png")); //$NON-NLS-1$
	}
	protected boolean dirty = true;
	protected int getAccess;
	protected int fieldAccess;
	protected Point hotspotPoint;
	protected Component widget;
	protected String lastName;
	protected String name;
	protected String widgetName;
	protected boolean selected;
	protected Map<String, IConfigurationElement> propertyConfigs;
	protected Map<EventSetDescriptor, IEventListenerModel> eventDescriptor;
	protected Map<String, Boolean> edited;
	protected Image iconImage;
	protected List<InvisibleAdapter> invisibles = new ArrayList<InvisibleAdapter>();
	protected Map<String, Object> properties = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	public abstract Class getWidgetClass();

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
	public void setCursorType(int type){
		VisualDesigner designer = getDesigner();
		if(designer!=null)
			designer.setCursorType(type);
	}
	public void hideMenu() {
		Stack<MenuElement> stack = MenuSelectionManager.defaultManager().getSelectionStack();
		while (!stack.isEmpty()) {
			MenuElement me = stack.pop();
			if (me instanceof JMenu) {
				JMenu jme = (JMenu) me;
				jme.setPopupMenuVisible(false);
				jme.setSelected(false);
			}
		}
	}
	@Override
	public void requestNewName() {
		if (getName() == null) {
			setName(getNamespace().nextName(getBasename()));
		}
	}

	@Override
	public String getBasename() {
		return NamespaceUtil.getBasename(getWidgetClass());
	}

	public void lockDesigner() {
		VisualDesigner designer = getDesigner();
		if (designer != null)
			designer.lock();
	}

	public void unlockDesigner() {
		VisualDesigner designer = getDesigner();
		if (designer != null)
			designer.unlock();
	}

	public ICompilationUnit getCompilationUnit() {
		VisualDesigner designer = getDesigner();
		if (designer != null)
			return designer.getCompilationUnit();
		else
			return null;
	}

	public void setProperty(String key, Object value) {
		if (value == null)
			properties.remove(key);
		else
			properties.put(key, value);
	}

	public Object getProperty(String key) {
		return properties.get(key);
	}

	public List<InvisibleAdapter> getInvisibles() {
		return invisibles;
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
		}
		if (getDesigner() != null)
			getDesigner().fireDirty();
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
			return "[" + getWidgetName() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return getName() + " [" + getWidgetName() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	protected boolean editValue() {
		return getDesigner().editComponent(getWidget());
	}

	public void doLayout() {
		if (getWidget() instanceof Container)
			layoutContainer((Container) getWidget());
	}

	public List<Component> getSelectedComponents() {
		if (getDesigner() != null) {
			return getDesigner().getSelectedComponents();
		}
		return null;
	}

	protected void attach() {
		if (widget instanceof RootPaneContainer) {
			JRootPane jrootPane = ((RootPaneContainer) widget).getRootPane();
			jrootPane.putClientProperty(ADAPTER_PROPERTY, this);
		} else if (widget instanceof JComponent) {
			((JComponent) widget).putClientProperty(ADAPTER_PROPERTY, this);
		}
	}

	protected WidgetAdapter() {
		this.eventDescriptor = new HashMap<EventSetDescriptor, IEventListenerModel>();
		this.edited = new HashMap<String, Boolean>();
	}

	public NamespaceManager getNamespace() {
		VisualDesigner designer = getDesigner();
		if (designer != null)
			return designer.getNamespace();
		return null;
	}

	protected WidgetAdapter(String name) {
		setName(name);
		this.widget = createWidget();
		this.hotspotPoint = new Point(widget.getWidth() / 2,
				widget.getHeight() / 2);
		attach();
		this.eventDescriptor = new HashMap<EventSetDescriptor, IEventListenerModel>();
		this.edited = new HashMap<String, Boolean>();
	}

	public Map<String, Boolean> getEdited() {
		return edited;
	}

	public void setWidget(Component widget) {
		this.widget = widget;
		attach();
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

	@Override
	public String getCreationMethodName() {
		return NamespaceUtil.getGetMethodName(getName());
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
				return focused.allowChildResize();
			} else
				return false;
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (this.name == null || !this.name.equals(name)) {
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
		Component w = getRootPane();
		if (w == null)
			return null;
		Component parent = w;
		while (parent != null && !(parent instanceof VisualDesigner)) {
			if(parent instanceof JPopupMenu){
				parent = ((JPopupMenu)parent).getInvoker();
			}
			parent = parent.getParent();
		}
		if (parent == null)
			return null;
		return (VisualDesigner) parent;
	}

	public void repaintDesigner() {
		if (getDesigner() != null) {
			getDesigner().clearCapture();
			getDesigner().repaint();
		}
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
		if (comp instanceof RootPaneContainer) {
			Container content = ((RootPaneContainer) comp).getRootPane();
			if (content instanceof JComponent) {
				return (WidgetAdapter) ((JComponent) content)
						.getClientProperty(ADAPTER_PROPERTY);
			}
		} else if (comp instanceof JComponent)
			return (WidgetAdapter) ((JComponent) comp)
					.getClientProperty(ADAPTER_PROPERTY);
		return null;
	}

	public Border getDesignBorder() {
		return BorderFactory.createLineBorder(new Color(224, 224, 255), 4);
	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		widgetName = config.getAttribute("widgetName"); //$NON-NLS-1$
		String sIcon = config.getAttribute("icon"); //$NON-NLS-1$
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
			String widgetClassname = config.getAttribute("widgetClass"); //$NON-NLS-1$
			IContributor contributor = config.getContributor();
			String pluginId = contributor.getName();
			Bundle bundle = Platform.getBundle(pluginId);
			return bundle.loadClass(widgetClassname);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
			return null;
		}
	}

	private HashMap<String, IConfigurationElement> parseProperties(
			IConfigurationElement config) {
		try {
			@SuppressWarnings("unchecked")
			Class widgetClass = getWidgetClass(config);
			HashMap<String, IConfigurationElement> eProperties = new HashMap<String, IConfigurationElement>();
			IConfigurationElement[] props = config.getChildren("property"); //$NON-NLS-1$
			for (IConfigurationElement prop : props) {
				String propertyId = prop.getAttribute("id"); //$NON-NLS-1$
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
			VisualSwingPlugin.getLogger().error(e);
			return null;
		}
	}

	public IPropertySource getPropertySource(Object object) {
		ArrayList<IWidgetPropertyDescriptor> propdesc = getPropertyDescriptors();
		if (object instanceof WidgetSelection) {
			WidgetSelection selection = (WidgetSelection) object;
			if (!isRoot() && selection.size() == 1) {
				propdesc.add(new BeanNameProperty(this));
				propdesc.add(new FieldAccessProperty(this));
				propdesc.add(new GetAccessProperty(this));
			}
			IWidgetPropertyDescriptor[] properties = propdesc
					.toArray(new IWidgetPropertyDescriptor[propdesc.size()]);
			String lnfClassname = null;
			if (!selection.isEmpty()) {
				lnfClassname = getLnfClassname();
			}
			return new PropertySource2(lnfClassname, selection, properties);
		} else {
			if (!isRoot()) {
				propdesc.add(new BeanNameProperty(this));
				propdesc.add(new FieldAccessProperty(this));
				propdesc.add(new GetAccessProperty(this));
			}
			IWidgetPropertyDescriptor[] properties = propdesc
					.toArray(new IWidgetPropertyDescriptor[propdesc.size()]);
			return new PropertySource2(getLnfClassname(),
					new StructuredSelection(new Object[] { object }),
					properties);
		}
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

	public void setFieldAccess(int fieldAccess) {
		this.fieldAccess = fieldAccess;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<IWidgetPropertyDescriptor> getPropertyDescriptors() {
		Sorting sorting = ExtensionRegistry.getCurrentSorting();
		HashMap<String, String> references = new HashMap<String, String>();
		Class beanClass = getWidgetClass();
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
		String sClass = config.getAttribute("class"); //$NON-NLS-1$
		if (sClass != null && sClass.trim().length() > 0) {
			IWidgetPropertyDescriptor iwpd;
			try {
				iwpd = (IWidgetPropertyDescriptor) config
						.createExecutableExtension("class"); //$NON-NLS-1$
				iwpd.init(config, beanClass);
				return iwpd;
			} catch (CoreException e) {
				VisualSwingPlugin.getLogger().error(e);
			}
		}
		return new WidgetProperty(config, beanClass);
	}

	private void mergeProperties(
			HashMap<String, IConfigurationElement> eSources,
			HashMap<String, IConfigurationElement> eTargets) {
		for (IConfigurationElement eTarget : eTargets.values()) {
			String eId = eTarget.getAttribute("id"); //$NON-NLS-1$
			if (eSources.get(eId) == null) {
				eSources.put(eId, eTarget);
			}
		}
	}

	public WidgetAdapter getRootAdapter() {
		Component w = getRootPane();
		if (w == null)
			return null;
		if (isRoot())
			return this;
		WidgetAdapter parent = getParentAdapter();
		if (parent == null)
			return null;
		return parent.getRootAdapter();
	}

	public boolean isRoot() {
		Component me = getRootPane();
		if (me == null)
			return false;
		Container container = me.getParent();
		return container == null || container instanceof VisualDesigner;
	}

	public IAdapter getParent() {
		return getParentAdapter();
	}

	public CompositeAdapter getParentAdapter() {
		if (isRoot())
			return null;
		Component me = getRootPane();
		if (me == null)
			return null;
		Component parent = me.getParent();
		while (parent != null) {
			if (parent instanceof Component) {
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(parent);
				if (adapter != null) {
					if (adapter.getDelegateAdapter() != null)
						return (CompositeAdapter) adapter.getDelegateAdapter();
					return (CompositeAdapter) adapter;
				}
			}
			parent = parent.getParent();
		}
		return null;
	}

	protected WidgetAdapter getDelegateAdapter() {
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

	protected void paintForbiddenMascot(Graphics g) {
		Point p = getMascotLocation();
		FORBIDDEN_ICON.paintIcon(getWidget(), g, p.x - 16, p.y - 16);
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

	public List<WidgetAdapter> getDropWidget() {
		return WhiteBoard.getSelectedWidget() == null ? EMPTY_LIST : WhiteBoard
				.getSelectedWidget();
	}

	private static List<WidgetAdapter> EMPTY_LIST = new ArrayList<WidgetAdapter>(
			0);

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
		MenuManager eventMenu = new MenuManager(
				Messages.WidgetAdapter_Add_Edit_Events, "#EVENT"); //$NON-NLS-2$
		fillAddEventAction(eventMenu);
		menu.add(eventMenu);
		MenuManager delEventMenu = new MenuManager(
				Messages.WidgetAdapter_Delete_Events, "#DELETE_EVENT"); //$NON-NLS-1$
		fillDelEventAction(delEventMenu);
		menu.add(delEventMenu);
		MenuManager borderMenu = new MenuManager(Messages.WidgetAdapter_Border,
				"#BORDER"); //$NON-NLS-2$
		fillBorderAction(borderMenu);
		menu.add(borderMenu);
		if (!isRoot()) {
			CompositeAdapter parentAdapter = getParentAdapter();
			if (parentAdapter != null) {
				parentAdapter.fillConstraintsAction(menu, getWidget());
			}
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
					"#DELETE_EVENT_" + key); //$NON-NLS-1$
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
			Class<?> clazz = getWidgetClass();
			return Introspector.getBeanInfo(clazz);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		return null;
	}

	protected void fillAddEventAction(MenuManager eventMenu) {
		EventSetDescriptor[] esds = getBeanInfo().getEventSetDescriptors();
		for (EventSetDescriptor esd : esds) {
			MenuManager subEventMenu = new MenuManager(esd.getName(),
					"#ADD_EVENT_" + esd.getName()); //$NON-NLS-1$
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
			if (property.isPropertySet(getLnfClassname(),
					new StructuredSelection(getWidget()))) {
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

	public Rectangle getDesignBounds() {
		int w = getWidget().getWidth();
		if (w <= 0)
			w = 400;
		int h = getWidget().getHeight();
		if (h <= 0)
			h = 300;
		return new Rectangle(24, 24, w, h);
	}

	public String getWidgetCodeClassName() {
		return getWidgetClass().getName();
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

	public boolean needGlobalGraphics() {
		return false;
	}

	public void deleteNotify() {
	}

	public int getFieldAccess() {
		return fieldAccess;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class getObjectClass() {
		return this.getWidgetClass();
	}

	public void addInvisible(String name, Object object) {
		invisibles.add(InvisibleAdapter.createAdapter(name, object));
	}

	public boolean isRenamed() {
		if (lastName != null && !lastName.equals(name))
			return true;
		if (isRoot()) {
			for (InvisibleAdapter inv : invisibles) {
				if (inv!=null&&inv.isRenamed())
					return true;
			}
		}
		return false;
	}

	public boolean includeName(String another) {
		if (name != null && name.equals(another))
			return true;
		if (isRoot()) {
			for (InvisibleAdapter invisible : getInvisibles()) {
				if (invisible!=null&&invisible.getName() != null
						&& invisible.getName().equals(another)) {
					return true;
				}
			}
		}
		return false;
	}
	public CompositeAdapter getFocusedAdapter(){
		VisualDesigner designer = getDesigner();
		if(designer!=null)
			return designer.getFocusedContainer();
		return null;
	}
	public boolean isFocused() {
		return getFocusedAdapter()==this;
	}

	public Component getContentPane() {
		return getWidget();
	}

	public Component getRootPane() {
		return getWidget();
	}

	public Component getComponent() {
		return getWidget();
	}

	public Component getWidget() {
		if (widget == null) {
			widget = createWidget();
			if (widget != null)
				attach();
		}
		return widget;
	}
}
