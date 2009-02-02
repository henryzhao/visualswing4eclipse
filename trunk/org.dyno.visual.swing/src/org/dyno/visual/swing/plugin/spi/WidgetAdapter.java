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
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.Introspector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.LookAndFeel;
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
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.base.NamespaceManager;
import org.dyno.visual.swing.base.PropertySource2;
import org.dyno.visual.swing.base.WidgetProperty;
import org.dyno.visual.swing.base.ExtensionRegistry.Category;
import org.dyno.visual.swing.base.ExtensionRegistry.Provider;
import org.dyno.visual.swing.base.ExtensionRegistry.Sorting;
import org.dyno.visual.swing.designer.GlassPlane;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.designer.WidgetSelection;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;
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
@SuppressWarnings("unchecked")
public abstract class WidgetAdapter extends AbstractAdaptable implements IExecutableExtension, Cloneable, IPropertySourceProvider, IConstants, IAdapter {

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
	protected Image iconImage;
	protected List<InvisibleAdapter> invisibles = new ArrayList<InvisibleAdapter>();

	public abstract Class getWidgetClass();

	public String getID() {
		if (lastName != null)
			return lastName;
		else
			return name;
	}

	public void setCursorType(int type) {
		VisualDesigner designer = getDesigner();
		if (designer != null)
			designer.setCursorType(type);
	}

	@Override
	public void requestNewName() {
		if (getName() == null) {
			setName(getNamespace().nextName(getBasename()));
		}
	}

	public void requestGlobalNewName() {
		if (getName() == null) {
			VisualDesigner designer = WhiteBoard.getCurrentDesigner();
			if (designer != null) {
				NamespaceManager namespace = designer.getNamespace();
				if (namespace != null)
					setName(namespace.nextName(getBasename()));
			}
		}
	}

	@Override
	public String getBasename() {
		String className = getWidgetClass().getName();
		int dot = className.lastIndexOf('.');
		if (dot != -1)
			className = className.substring(dot + 1);
		return Character.toLowerCase(className.charAt(0)) + className.substring(1);
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
			return "[" + getWidgetName() + "]";
		} else {
			return getName() + " [" + getWidgetName() + "]";
		}
	}

	public boolean editValue() {
		return getDesigner().editComponent(getWidget());
	}

	public void doLayout() {
		if (getWidget() instanceof Container)
			JavaUtil.layoutContainer((Container) getWidget());
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
	}

	public NamespaceManager getNamespace() {
		VisualDesigner designer = getDesigner();
		if (designer == null)
			designer = WhiteBoard.getCurrentDesigner();
		return designer == null ? null : designer.getNamespace();
	}

	protected WidgetAdapter(String name) {
		setName(name);
		this.widget = createWidget();
		this.hotspotPoint = new Point(widget.getWidth() / 2, widget.getHeight() / 2);
		attach();
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

	public boolean isResizable() {
		if (isRoot())
			return false;
		CompositeAdapter focused = getCurrentFocused();
		if (focused == null)
			return false;
		else {
			Component comp = focused.getWidget();
			if (comp instanceof Container) {
				return focused.allowChildResize(getWidget());
			} else
				return false;
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
			if (parent instanceof JPopupMenu) {
				parent = ((JPopupMenu) parent).getInvoker();
			}
			parent = parent.getParent();
		}
		if (parent == null)
			return WhiteBoard.getCurrentDesigner();
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
				return (WidgetAdapter) ((JComponent) content).getClientProperty(ADAPTER_PROPERTY);
			}
		} else if (comp instanceof JComponent)
			return (WidgetAdapter) ((JComponent) comp).getClientProperty(ADAPTER_PROPERTY);
		return null;
	}

	public Border getDesignBorder() {
		return BorderFactory.createLineBorder(new Color(224, 224, 255), 4);
	}

	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		widgetName = config.getAttribute("widgetName"); //$NON-NLS-1$
		String sIcon = config.getAttribute("icon"); //$NON-NLS-1$
		if (sIcon != null && sIcon.trim().length() > 0) {
			IContributor contributor = config.getContributor();
			String pluginId = contributor.getName();
			this.iconImage = VisualSwingPlugin.getSharedImage(pluginId, sIcon);
		}
		propertyConfigs = parseProperties(config); // ...
	}

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

	private HashMap<String, IConfigurationElement> parseProperties(IConfigurationElement config) {
		try {
			Class widgetClass = getWidgetClass(config);
			HashMap<String, IConfigurationElement> eProperties = new HashMap<String, IConfigurationElement>();
			IConfigurationElement[] props = config.getChildren("property"); //$NON-NLS-1$
			for (IConfigurationElement prop : props) {
				String propertyId = prop.getAttribute("id"); //$NON-NLS-1$
				eProperties.put(propertyId, prop);
			}
			if (widgetClass != Component.class) {
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
			IWidgetPropertyDescriptor[] properties = propdesc.toArray(new IWidgetPropertyDescriptor[propdesc.size()]);
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
			IWidgetPropertyDescriptor[] properties = propdesc.toArray(new IWidgetPropertyDescriptor[propdesc.size()]);
			return new PropertySource2(getLnfClassname(), new StructuredSelection(new Object[] { object }), properties);
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

	public ArrayList<IWidgetPropertyDescriptor> getPropertyDescriptors() {
		Sorting sorting = ExtensionRegistry.getCurrentSorting();
		HashMap<String, String> references = new HashMap<String, String>();
		Class beanClass = getWidgetClass();
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

	private IWidgetPropertyDescriptor createProperty(IConfigurationElement config, Class beanClass) {
		String sClass = config.getAttribute("class"); //$NON-NLS-1$
		if (sClass != null && sClass.trim().length() > 0) {
			IWidgetPropertyDescriptor iwpd;
			try {
				iwpd = (IWidgetPropertyDescriptor) config.createExecutableExtension("class"); //$NON-NLS-1$
				iwpd.init(config, beanClass);
				return iwpd;
			} catch (CoreException e) {
				VisualSwingPlugin.getLogger().error(e);
			}
		}
		return new WidgetProperty(config, beanClass);
	}

	private void mergeProperties(HashMap<String, IConfigurationElement> eSources, HashMap<String, IConfigurationElement> eTargets) {
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
			return parentAdapter.isChildMoveable(getWidget());
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

	public List<WidgetAdapter> getDropWidget() {
		return WhiteBoard.getSelectedWidget() == null ? EMPTY_LIST : WhiteBoard.getSelectedWidget();
	}

	private static List<WidgetAdapter> EMPTY_LIST = new ArrayList<WidgetAdapter>(0);

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
			return SwingUtilities.convertPoint(designer, p, getContentArea());
		} else
			return p;
	}

	public Component getContentArea() {
		return getWidget();
	}

	public Point convertToGlobal(Point p) {
		VisualDesigner designer = getDesigner();
		if (designer != null) {
			return SwingUtilities.convertPoint(getContentArea(), p, designer);
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

	public BeanInfo getBeanInfo() {
		try {
			Class<?> clazz = getWidgetClass();
			return Introspector.getBeanInfo(clazz);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		return null;
	}

	public Object clone() {
		return ExtensionRegistry.createAdapterFor(cloneWidget());
	}

	protected abstract Component newWidget();

	public Component cloneWidget() {
		Component clone = newWidget();
		ArrayList<IWidgetPropertyDescriptor> properties = getPropertyDescriptors();
		for (IWidgetPropertyDescriptor property : properties) {
			if (property.isPropertySet(getLnfClassname(), new StructuredSelection(getWidget()))) {
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

	public boolean needGlobalGraphics() {
		return false;
	}

	public void deleteNotify() {
	}

	public int getFieldAccess() {
		return fieldAccess;
	}

	@Override
	protected Class getObjectClass() {
		return this.getWidgetClass();
	}

	public void addInvisible(String name, Object object) {
		invisibles.add(InvisibleAdapter.createAdapter(getRootAdapter(), name, object));
	}

	public boolean isRenamed() {
		if (lastName != null && !lastName.equals(name))
			return true;
		if (isRoot()) {
			for (InvisibleAdapter inv : invisibles) {
				if (inv != null && inv.isRenamed())
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
				if (invisible != null && invisible.getName() != null && invisible.getName().equals(another)) {
					return true;
				}
			}
		}
		return false;
	}

	public CompositeAdapter getFocusedAdapter() {
		VisualDesigner designer = getDesigner();
		if (designer != null)
			return designer.getSelectedContainer();
		return null;
	}

	public boolean isFocused() {
		return getFocusedAdapter() == this;
	}

	public Component getWidget() {
		if (widget == null) {
			widget = createWidget();
			if (widget != null)
				attach();
		}
		return widget;
	}

	public Component getParentContainer() {
		return getWidget();
	}

	public Component getRootPane() {
		return getWidget();
	}

	public Component getContentPane() {
		return getWidget();
	}

	public Map<EventSetDescriptor, IEventListenerModel> getEventDescriptor() {
		Map<EventSetDescriptor, IEventListenerModel> map = (Map<EventSetDescriptor, IEventListenerModel>) getProperty("event.descriptor");
		if (map == null) {
			map = new HashMap<EventSetDescriptor, IEventListenerModel>();
			setProperty("event.descriptor", map);
		}
		return map;
	}

	public Map<String, Boolean> getEditingMap() {
		Map<String, Boolean> editingMap = (Map<String, Boolean>) getProperty("editing.map");
		if (editingMap == null) {
			editingMap = new HashMap<String, Boolean>();
			setProperty("editing.map", editingMap);
		}
		return editingMap;
	}
	
	public void setPreferredLookAndFeel(String lnf){
		setProperty("preferred.lookandfeel", lnf);
	}
	public String getPreferredLookAndFeel(){
		return (String) getProperty("preferred.lookandfeel");
	}
}
