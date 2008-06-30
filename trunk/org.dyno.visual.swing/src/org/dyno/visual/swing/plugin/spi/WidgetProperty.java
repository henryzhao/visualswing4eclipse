package org.dyno.visual.swing.plugin.spi;

import java.awt.Component;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Comparator;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

import org.dyno.visual.swing.base.NamespaceManager;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class WidgetProperty implements IWidgetPropertyDescriptor {
	private Object lastValue;

	private String category;
	private ICellEditorFactory editorFactory;
	private ILabelProviderFactory labelFactory;
	private boolean editable;
	private PropertyDescriptor propertyDescriptor;
	private String helpContextId;
	private String description;
	private String propertyName;
	private String id;
	private String displayName;
	private String[] filters;
	@SuppressWarnings("unchecked")
	private Class beanClass;
	private boolean gencode;

	@SuppressWarnings("unchecked")
	public WidgetProperty(String id, String name, Class beanClass, ILabelProviderFactory label, ICellEditorFactory editor) {
		this.beanClass = beanClass;
		this.id = id;
		this.propertyName = name;
		try {
			propertyDescriptor = new PropertyDescriptor(propertyName, beanClass);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		gencode = true;
		displayName = name;
		editable = true;
		labelFactory = label;
		editorFactory = editor;
	}

	public WidgetProperty() {

	}

	@SuppressWarnings("unchecked")
	public WidgetProperty(String id, String name, Class beanClass) {
		this.beanClass = beanClass;
		this.id = id;
		this.propertyName = name;
		try {
			propertyDescriptor = new PropertyDescriptor(propertyName, beanClass);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		gencode = true;
		displayName = name;
		editable = true;
		Class<?> type = propertyDescriptor.getPropertyType();
		TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
		if (ta != null) {
			labelFactory = ta.getRenderer();
			editorFactory = ta.getEditor();
		}
	}

	@SuppressWarnings("unchecked")
	public WidgetProperty(IConfigurationElement config, Class beanClass) {
		init(config, beanClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(IConfigurationElement config, Class beanClass) {
		this.beanClass = beanClass;
		id = config.getAttribute("id");
		propertyName = config.getAttribute("name");
		try {
			propertyDescriptor = new PropertyDescriptor(propertyName, beanClass);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		String sGencode = config.getAttribute("gencode");
		if (sGencode == null || sGencode.trim().length() == 0)
			gencode = true;
		else
			gencode = sGencode.toLowerCase().equals("true");
		displayName = config.getAttribute("displayName");
		if (displayName == null || displayName.trim().length() == 0)
			displayName = propertyName;
		String sEditable = config.getAttribute("editable");
		editable = sEditable == null || sEditable.trim().length() == 0 || sEditable.toLowerCase().equals("true");
		String sLabel = config.getAttribute("renderer");
		Class<?> type = propertyDescriptor.getPropertyType();
		TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
		if (sLabel != null && sLabel.trim().length() > 0) {
			createLabelProviderFactory(config);
		} else if (ta != null) {
			labelFactory = ta.getRenderer();
		}
		String sEditor = config.getAttribute("editor");
		if (sEditor != null && sEditor.trim().length() > 0) {
			createEditorProviderFactory(config);
		} else if (ta != null) {
			editorFactory = ta.getEditor();
		}
		helpContextId = config.getAttribute("help-context-id");
		if (helpContextId != null && helpContextId.trim().length() == 0)
			helpContextId = null;
		IConfigurationElement[] children = config.getChildren("description");
		if (children != null && children.length > 0) {
			description = children[0].getValue();
		}
		children = config.getChildren("filter");
		if (children != null && children.length > 0) {
			filters = new String[children.length];
			for (int i = 0; i < children.length; i++) {
				filters[i] = children[i].getValue();
			}
		}
	}

	private void createEditorProviderFactory(IConfigurationElement config) {
		try {
			editorFactory = (ICellEditorFactory) config.createExecutableExtension("editor");
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void createLabelProviderFactory(IConfigurationElement config) {
		try {
			labelFactory = (ILabelProviderFactory) config.createExecutableExtension("renderer");
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public void setCategory(String category) {
		this.category = category;
	}

	private Object bean;

	public void setBean(Object bean) {
		this.bean = bean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getPropertyValue(Object bean) {
		Method readMethod = propertyDescriptor.getReadMethod();
		if (readMethod != null) {
			try {
				Object value = readMethod.invoke(bean);
				if (isEditable()) {
					if (editorFactory != null)
						value = editorFactory.encodeValue(value);
					else {
						Class type = lastValue.getClass();
						TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
						value = ta.getEditor().decodeValue(value);
					}
				}
				lastValue = value;
				return value;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		lastValue = null;
		return null;
	}

	private Object _getPropertyValue(Object bean) {
		Method readMethod = propertyDescriptor.getReadMethod();
		if (readMethod != null) {
			try {
				return readMethod.invoke(bean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean isPropertyResettable(Object bean) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isPropertySet(Object bean) {
		String name = propertyDescriptor.getName();
		if (name.equals("preferredSize") && bean instanceof Component) {
			return ((Component) bean).isPreferredSizeSet();
		} else if (name.equals("minimumSize") && bean instanceof Component)
			return ((Component) bean).isMinimumSizeSet();
		else if (name.equals("maximumSize") && bean instanceof Component)
			return ((Component) bean).isMaximumSizeSet();
		Object value = _getPropertyValue(bean);
		if (value != null && value instanceof UIResource)
			return false;
		Class<?> propertyType = propertyDescriptor.getPropertyType();
		LookAndFeel lnf = UIManager.getLookAndFeel();
		String lnfClass;
		if (lnf == null)
			lnfClass = UIManager.getCrossPlatformLookAndFeelClassName();
		else
			lnfClass = lnf.getClass().getName();
		ILookAndFeelAdapter adapter = ExtensionRegistry.getLnfAdapter(lnfClass);
		if (adapter == null)
			return false;
		Object default_value = adapter.getDefaultValue(beanClass, propertyName);
		if (propertyType == byte.class) {
			byte bv = value == null ? 0 : ((Byte) value).byteValue();
			byte dv = default_value == null ? 0 : ((Byte) default_value).byteValue();
			return bv != dv;
		} else if (propertyType == char.class) {
			char bv = value == null ? 0 : ((Character) value).charValue();
			char dv = default_value == null ? 0 : ((Character) default_value).charValue();
			return bv != dv;
		} else if (propertyType == short.class) {
			short bv = value == null ? 0 : ((Short) value).shortValue();
			short dv = default_value == null ? 0 : ((Short) default_value).shortValue();
			return bv != dv;
		} else if (propertyType == int.class) {
			int bv = value == null ? 0 : ((Integer) value).intValue();
			int dv = default_value == null ? 0 : ((Integer) default_value).intValue();
			return bv != dv;
		} else if (propertyType == long.class) {
			long bv = value == null ? 0 : ((Long) value).longValue();
			long dv = default_value == null ? 0 : ((Long) default_value).longValue();
			return bv != dv;
		} else if (propertyType == float.class) {
			float bv = value == null ? 0 : ((Float) value).floatValue();
			float dv = default_value == null ? 0 : ((Float) default_value).floatValue();
			return bv != dv;
		} else if (propertyType == double.class) {
			double bv = value == null ? 0 : ((Double) value).doubleValue();
			double dv = default_value == null ? 0 : ((Double) default_value).doubleValue();
			return bv != dv;
		} else if (propertyType == void.class) {
			return false;
		} else if (propertyType == boolean.class) {
			boolean bv = value == null ? false : ((Boolean) value).booleanValue();
			boolean dv = default_value == null ? false : ((Boolean) default_value).booleanValue();
			return bv != dv;
		} else if (propertyType == Byte.class) {
			byte bv = value == null ? 0 : ((Byte) value).byteValue();
			byte dv = default_value == null ? 0 : ((Byte) default_value).byteValue();
			return bv != dv;
		} else if (propertyType == Character.class) {
			char bv = value == null ? 0 : ((Character) value).charValue();
			char dv = default_value == null ? 0 : ((Character) default_value).charValue();
			return bv != dv;
		} else if (propertyType == Short.class) {
			short bv = value == null ? 0 : ((Short) value).shortValue();
			short dv = default_value == null ? 0 : ((Short) default_value).shortValue();
			return bv != dv;
		} else if (propertyType == Integer.class) {
			int bv = value == null ? 0 : ((Integer) value).intValue();
			int dv = default_value == null ? 0 : ((Integer) default_value).intValue();
			return bv != dv;
		} else if (propertyType == Long.class) {
			long bv = value == null ? 0 : ((Long) value).longValue();
			long dv = default_value == null ? 0 : ((Long) default_value).longValue();
			return bv != dv;
		} else if (propertyType == Float.class) {
			float bv = value == null ? 0 : ((Float) value).floatValue();
			float dv = default_value == null ? 0 : ((Float) default_value).floatValue();
			return bv != dv;
		} else if (propertyType == Double.class) {
			double bv = value == null ? 0 : ((Double) value).doubleValue();
			double dv = default_value == null ? 0 : ((Double) default_value).doubleValue();
			return bv != dv;
		} else if (propertyType == Void.class) {
			return false;
		} else if (propertyType == Boolean.class) {
			boolean bv = value == null ? false : ((Boolean) value).booleanValue();
			boolean dv = default_value == null ? false : ((Boolean) default_value).booleanValue();
			return bv != dv;
		} else {
			if (value == null && default_value == null)
				return false;
			else if (value == null && default_value != null)
				return true;
			else if (value != null && default_value == null)
				return true;
			else {
				TypeAdapter typeAdapter = ExtensionRegistry.getTypeAdapter(propertyType);
				if (typeAdapter != null) {
					Comparator comparator = typeAdapter.getComparator();
					if (comparator != null)
						return comparator.compare(value, default_value) != 0;
				}
				return !value.equals(default_value);
			}
		}
	}

	@Override
	public void resetPropertyValue(Object bean) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setPropertyValue(Object bean, Object value) {
		if (isEditable()) {
			try {
				if (editorFactory != null)
					value = editorFactory.decodeValue(value);
				else {
					Class type = lastValue.getClass();
					TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
					value = ta.getEditor().decodeValue(value);
				}
				propertyDescriptor.getWriteMethod().invoke(bean, value);
				if (bean instanceof JComponent) {
					JComponent jcomp = (JComponent) bean;
					WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(jcomp);
					adapter.setDirty(true);
					if (adapter != null) {
						adapter.getDesigner().repaint();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private boolean isEditable() {
		if (editorFactory == null) {
			if (lastValue == null)
				return false;
			else {
				Class type = lastValue.getClass();
				TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
				if (ta != null && ta.getEditor() != null)
					return true;
				else
					return false;
			}
		}
		if (editable) {
			Method setMethod = propertyDescriptor.getWriteMethod();
			if (setMethod == null) {
				return false;
			} else {
				return true;
			}
		} else
			return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		if (isEditable()) {
			if (editorFactory != null)
				return editorFactory.createPropertyEditor(bean, parent);
			else {
				Class type = lastValue.getClass();
				TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
				return ta.getEditor().createPropertyEditor(bean, parent);
			}
		} else {
			return null;
		}
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String[] getFilterFlags() {
		return filters;
	}

	public void setFilterFlags(String[] filters) {
		this.filters = filters;
	}

	@Override
	public Object getHelpContextIds() {
		return helpContextId;
	}

	@Override
	public Object getId() {
		return id;
	}

	@Override
	public ILabelProvider getLabelProvider() {
		return labelFactory == null ? null : labelFactory.getLabelProvider();
	}

	@Override
	public boolean isCompatibleWith(IPropertyDescriptor anotherProperty) {
		return false;
	}

	@Override
	public boolean cloneProperty(Object bean, JComponent clone) {
		Object value = null;
		try {
			value = propertyDescriptor.getReadMethod().invoke(bean);
		} catch (Exception e) {
			return false;
		}
		if (value != null) {
			TypeAdapter adapter = ExtensionRegistry.getTypeAdapter(value.getClass());
			if (adapter != null && adapter.getCloner() != null) {
				value = adapter.getCloner().clone(value);
			}
		}
		try {
			propertyDescriptor.getWriteMethod().invoke(clone, value);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getSetCode(Object bean, ImportRewrite imports) {
		if (bean instanceof JComponent) {
			JComponent comp = (JComponent) bean;
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(comp);
			if (adapter != null) {
				StringBuilder builder = new StringBuilder();
				Object value = _getPropertyValue(bean);
				ICodeGen gen = null;
				Class typeClass = propertyDescriptor.getPropertyType();
				TypeAdapter typeAdapter = ExtensionRegistry.getTypeAdapter(typeClass);
				if (typeAdapter != null) {
					gen = typeAdapter.getEndec();
				}
				if (gen == null)
					gen = editorFactory;
				if (gen != null && value != null) {
					String initCode = gen.getInitJavaCode(value, imports);
					if (initCode != null)
						builder.append(initCode);
				}
				if (!adapter.isRoot()) {
					builder.append(getFieldName(adapter.getName()) + ".");
				}
				builder.append(propertyDescriptor.getWriteMethod().getName() + "(");
				if (gen != null) {
					if (value == null) {
						builder.append("null");						
					} else {
						builder.append(gen.getJavaCode(value, imports));
					}
				} else {
					builder.append(value == null ? "null" : value.toString());
				}
				builder.append(");\n");
				return builder.toString();
			} else
				return null;
		} else
			return null;
	}

	private String getFieldName(String name) {
		return NamespaceManager.getInstance().getFieldName(name);
	}

	@Override
	public boolean isGencode() {
		return gencode;
	}
}
