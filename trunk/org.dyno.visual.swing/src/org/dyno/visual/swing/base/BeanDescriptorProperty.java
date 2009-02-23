package org.dyno.visual.swing.base;

import java.awt.Component;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;

import javax.swing.plaf.UIResource;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;
import org.dyno.visual.swing.plugin.spi.IValueParser;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.undo.SetValueOperation;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;


@SuppressWarnings("unchecked")
public class BeanDescriptorProperty extends PropertyAdapter implements IWidgetPropertyDescriptor {
	private Object lastValue;
	private Object default_value;

	private ICellEditorFactory editorFactory;
	private ILabelProviderFactory labelFactory;
	private PropertyDescriptor property;

	private IStructuredSelection bean;
	private String category;
	private Class beanClass;
	public BeanDescriptorProperty(PropertyDescriptor pd, Class beanClass) {
		this.property = pd;
		this.beanClass = beanClass;
		Class<?> type = pd.getPropertyType();
		TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
		if (ta != null) {
			labelFactory = ta.getRenderer();
			editorFactory = ta.getEditor();
		}
	}

	@Override
	public void init(IConfigurationElement config, Class beanClass) {
	}

	public void setBean(IStructuredSelection bean) {
		this.bean = bean;
	}

	
	@Override
	public Object getPropertyValue(IStructuredSelection bean) {
		assert !bean.isEmpty();
		try {
			Object value = getFieldValue(bean.getFirstElement());
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
			VisualSwingPlugin.getLogger().error(e);
		}
		lastValue = null;
		return null;
	}

	@Override
	public Object getFieldValue(Object bean) {
		try {
			Method readMethod = property.getReadMethod();
			if (readMethod != null) 
				return readMethod.invoke(bean);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		return null;
	}

	@Override
	public void setFieldValue(Object bean, Object newValue) {
		Method writeMethod = property.getWriteMethod();
		if(writeMethod!=null){
			try {
				writeMethod.invoke(bean, newValue);
			} catch (Exception e) {
				VisualSwingPlugin.getLogger().error(e);
			}
		}
	}
	@Override
	public boolean isPropertyResettable(IStructuredSelection bean) {
		return true;
	}

	
	@Override
	public boolean isPropertySet(String lnfClass, IStructuredSelection bean) {
		assert !bean.isEmpty();
		Object b=bean.getFirstElement();
		String name = property.getName();
		if (name.equals("preferredSize") && b instanceof Component) {
			return ((Component) b).isPreferredSizeSet();
		} else if (name.equals("minimumSize") && b instanceof Component)
			return ((Component) b).isMinimumSizeSet();
		else if (name.equals("maximumSize") && b instanceof Component)
			return ((Component) b).isMaximumSizeSet();
		Class<?> propertyType = property.getPropertyType();
		Object value = getFieldValue(b);
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
	public void resetPropertyValue(String lnfClassname, IStructuredSelection bean) {
		assert !bean.isEmpty();
		Object b = bean.getFirstElement();
		String propertyName = property.getName();
		if (propertyName.equals("preferredSize") && b instanceof Component) {
			return;
		} else if (propertyName.equals("minimumSize") && b instanceof Component)
			return;
		else if (propertyName.equals("maximumSize") && b instanceof Component)
			return;
		Object value = getFieldValue(b);
		if (value != null && value instanceof UIResource)
			return;
		
		Class<?> propertyType = property.getPropertyType();
		ILookAndFeelAdapter adapter = ExtensionRegistry.getLnfAdapter(lnfClassname);
		if (adapter == null)
			return;
		Object default_value = adapter.getDefaultValue(beanClass, propertyName);
		if (propertyType == byte.class) {
			byte dv = default_value == null ? 0 : ((Byte) default_value).byteValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == char.class) {
			char dv = default_value == null ? 0 : ((Character) default_value).charValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == short.class) {
			short dv = default_value == null ? 0 : ((Short) default_value).shortValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == int.class) {
			int dv = default_value == null ? 0 : ((Integer) default_value).intValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == long.class) {
			long dv = default_value == null ? 0 : ((Long) default_value).longValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == float.class) {
			float dv = default_value == null ? 0 : ((Float) default_value).floatValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == double.class) {
			double dv = default_value == null ? 0 : ((Double) default_value).doubleValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == void.class) {
		} else if (propertyType == boolean.class) {
			boolean dv = default_value == null ? false : ((Boolean) default_value).booleanValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == Byte.class) {
			byte dv = default_value == null ? 0 : ((Byte) default_value).byteValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == Character.class) {
			char dv = default_value == null ? 0 : ((Character) default_value).charValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == Short.class) {
			short dv = default_value == null ? 0 : ((Short) default_value).shortValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == Integer.class) {
			int dv = default_value == null ? 0 : ((Integer) default_value).intValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == Long.class) {
			long dv = default_value == null ? 0 : ((Long) default_value).longValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == Float.class) {
			float dv = default_value == null ? 0 : ((Float) default_value).floatValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == Double.class) {
			double dv = default_value == null ? 0 : ((Double) default_value).doubleValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == Void.class) {
		} else if (propertyType == Boolean.class) {
			boolean dv = default_value == null ? false : ((Boolean) default_value).booleanValue();
			setPropertyValue(bean, dv);
		} else {
			setPropertyValue(bean, default_value);
		}
	}

	
	@Override
	public void setPropertyValue(IStructuredSelection bean, Object value) {
		assert !bean.isEmpty();
		if (isEditable()) {
			try {
				if (editorFactory != null)
					value = editorFactory.decodeValue(value);
				else {
					Class type = lastValue.getClass();
					TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
					value = ta.getEditor().decodeValue(value);
				}
				for (Object b : bean.toArray()) {
					IUndoableOperation operation = new SetValueOperation(b, this, value);
					IOperationHistory operationHistory = PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
					if (b instanceof Component) {
						Component jcomp = (Component) b;
						WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(jcomp);
						if (adapter != null) {
							operation.addContext(adapter.getUndoContext());
						}
					}
					operationHistory.execute(operation, null, null);
				}
			} catch (Exception e) {
				VisualSwingPlugin.getLogger().error(e);
			}
		}
	}
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
		return property.getName();
	}

	@Override
	public String getDisplayName() {
		return property.getName();
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
		return property.getName();
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
	public boolean cloneProperty(Object bean, Component clone) {
		try {
			Object value = getFieldValue(bean);
			setFieldValue(clone, value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean isGencode() {
		return true;
	}

	@Override
	public void setFilterFlags(String[] filters) {
	}

	@Override
	public void setCategory(String categoryName) {
		this.category = categoryName;
	}

	@Override
	public boolean isEdited(WidgetAdapter adapter) {
		Map<String, Boolean> editedMap = adapter.getEditingMap();
		Boolean bool = editedMap.get(property.getName());
		return bool == null ? false : bool.booleanValue();
	}

	@Override
	public IValueParser getValueParser() {
		return null;
	}
	
	@Override
	protected Class getObjectClass() {
		return getClass();
	}
	@Override
	
	public Class getPropertyType(){
		return property.getPropertyType();
	}

	public String getSetMethodName() {
		 Method writeMethod = property.getWriteMethod();
		 if(writeMethod!=null)
			 return writeMethod.getName();
		 else
			 return null;
	}

}
