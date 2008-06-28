package org.dyno.visual.swing.plugin.spi;

import java.awt.Component;
import java.lang.reflect.Field;
import java.util.Comparator;

import javax.swing.JComponent;

import org.dyno.visual.swing.base.NamespaceManager;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class FieldProperty implements IWidgetPropertyDescriptor {
	private Object lastValue;
	private Object default_value;

	private ICellEditorFactory editorFactory;
	private ILabelProviderFactory labelFactory;
	private Field field;
	private String id;
	private String displayName;

	@SuppressWarnings("unchecked")
	public FieldProperty(String id, String name, Class beanClass) {
		this.id = id;
		try {
			field = getField(beanClass, name);
			field.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		displayName = name;
		Class<?> type = field.getType();
		TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
		if (ta != null) {
			labelFactory = ta.getRenderer();
			editorFactory = ta.getEditor();
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void init(IConfigurationElement config, Class beanClass) {
	}

	@SuppressWarnings("unchecked")
	public FieldProperty(String id, String name, Class beanClass, ILabelProviderFactory label, ICellEditorFactory editor) {
		this.id = id;
		try {
			field = getField(beanClass, name);
			field.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		displayName = name;
		labelFactory = label;
		editorFactory = editor;
	}

	@SuppressWarnings("unchecked")
	private Field getField(Class beanClass, String fieldName) {
		try {
			if (beanClass != null)
				return beanClass.getDeclaredField(fieldName);
			else
				return null;
		} catch (Exception e) {
			return getField(beanClass.getSuperclass(), fieldName);
		}
	}
	
	private Object bean;
	public void setBean(Object bean){
		this.bean = bean;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Object getPropertyValue(Object bean) {
		try {
			Object value = field.get(bean);
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
		lastValue = null;
		return null;
	}

	private Object _getPropertyValue(Object bean) {
		try {
			return field.get(bean);
		} catch (Exception e) {
			e.printStackTrace();
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
		String name = field.getName();
		if (name.equals("preferredSize") && bean instanceof Component) {
			return ((Component) bean).isPreferredSizeSet();
		} else if (name.equals("minimumSize") && bean instanceof Component)
			return ((Component) bean).isMinimumSizeSet();
		else if (name.equals("maximumSize") && bean instanceof Component)
			return ((Component) bean).isMaximumSizeSet();
		Class<?> propertyType = field.getType();
		Object value = _getPropertyValue(bean);
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
				field.set(bean, value);
				if (bean instanceof JComponent) {
					JComponent jcomp = (JComponent) bean;
					WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(jcomp);
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
		return true;
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
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String[] getFilterFlags() {
		return null;
	}

	@Override
	public Object getHelpContextIds() {
		return null;
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
	public void cloneProperty(Object bean, JComponent clone) {
		try {
			Object value = field.get(bean);
			field.set(clone, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getSetCode(Object bean, ImportRewrite imports) {
		if (bean instanceof JComponent) {
			JComponent comp = (JComponent) bean;
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(comp);
			if (adapter != null) {
				StringBuilder builder = new StringBuilder();
				Class typeClass = field.getType();
				TypeAdapter typeAdapter = ExtensionRegistry.getTypeAdapter(typeClass);
				Object value = _getPropertyValue(bean);
				if(typeAdapter!=null&&typeAdapter.getEndec()!=null){
					String initCode=typeAdapter.getEndec().getInitJavaCode(value, imports);
					if(initCode!=null)
						builder.append(initCode);
				}
				if (!adapter.isRoot()) {
					builder.append(getFieldName(adapter.getName()) + ".");
				}
				builder.append(field.getName() + "=");
				if (typeAdapter != null && typeAdapter.getEndec() != null) {
					builder.append(typeAdapter.getEndec().getJavaCode(value, imports));
				} else {
					builder.append(value == null ? "null" : value.toString());
				}
				builder.append(";\n");
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
		return false;
	}
	@Override
	public void setFilterFlags(String[] filters) {
	}
	@Override
	public void setCategory(String categoryName) {
	}
}
