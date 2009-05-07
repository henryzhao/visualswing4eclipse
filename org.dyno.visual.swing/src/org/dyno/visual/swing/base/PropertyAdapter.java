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

package org.dyno.visual.swing.base;

import java.awt.Component;
import java.awt.Container;
import java.util.Comparator;

import javax.swing.plaf.UIResource;

import org.dyno.visual.swing.plugin.spi.AbstractAdaptable;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.ISystemValue;
import org.dyno.visual.swing.plugin.spi.IValueParser;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

@SuppressWarnings("unchecked")
public class PropertyAdapter extends AbstractAdaptable implements IWidgetPropertyDescriptor {
	public boolean cloneProperty(Object bean, Component clone) {
		return false;
	}
	public Object getPropertyValue(IStructuredSelection bean) {
		return null;
	}
	public void init(IConfigurationElement config, Class beanClass) {
	}

	public boolean isEdited(WidgetAdapter adapter) {
		return false;
	}

	public boolean isGencode() {
		return false;
	}
	protected String getPropertyName(){
		return (String) getId();
	}
	protected Object getDefaultValue(Object b, String lnfClassname){
		return null;
	}
	public boolean isPropertySet(String lnfClassname, IStructuredSelection bean) {
		assert !bean.isEmpty();
		Object b = bean.getFirstElement();
		String name = getPropertyName();
		if (name.equals("preferredSize") && b instanceof Component) {
			return ((Component) b).isPreferredSizeSet();
		} else if (name.equals("minimumSize") && b instanceof Component)
			return ((Component) b).isMinimumSizeSet();
		else if (name.equals("maximumSize") && b instanceof Component)
			return ((Component) b).isMaximumSizeSet();
		else if (name.equals("layout") && b instanceof Container) {
			CompositeAdapter a = (CompositeAdapter) WidgetAdapter.getWidgetAdapter((Component) b);
			if (a.isDefaultLayout()) {
				return false;
			} else
				return true;
		}
		Object value = getFieldValue(b);
		if (value != null && value instanceof UIResource)
			return false;
		Object default_value = getDefaultValue(b, lnfClassname);
		Class<?> propertyType = getPropertyType();
		if (default_value instanceof ISystemValue)
			return true;
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
		} else if (propertyType == String.class) {
			if (value == null) {
				if (default_value == null) {
					return false;
				} else {
					String strDefault = (String) default_value;
					if (strDefault.trim().length() == 0)
						return false;
					else
						return true;
				}
			} else {
				if (default_value == null) {
					String strValue = (String) value;
					if (strValue.trim().length() == 0)
						return false;
					else
						return true;
				} else {
					return !value.equals(default_value);
				}
			}
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
	public void resetPropertyValue(String lnfClassname, IStructuredSelection bean) {
		assert !bean.isEmpty();
		Object b = bean.getFirstElement();
		String propertyName = getPropertyName();
		if (propertyName.equals("preferredSize") && b instanceof Component) {
			return;
		} else if (propertyName.equals("minimumSize") && b instanceof Component)
			return;
		else if (propertyName.equals("maximumSize") && b instanceof Component)
			return;
		Object value = getFieldValue(b);
		if (value != null && value instanceof UIResource)
			return;		
		Object default_value = getDefaultValue(b, lnfClassname);
		Class<?> propertyType = getPropertyType();
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


	
	public void setBean(IStructuredSelection bean) {
	}

	
	public void setCategory(String categoryName) {
	}

	
	public void setFilterFlags(String[] filters) {
	}

	
	public void setPropertyValue(IStructuredSelection bean, Object value) {
	}

	
	public CellEditor createPropertyEditor(Composite parent) {
		return null;
	}

	
	public String getCategory() {
		return null;
	}

	
	public String getDescription() {
		return null;
	}

	
	public String getDisplayName() {
		return null;
	}

	
	public String[] getFilterFlags() {
		return null;
	}

	
	public Object getHelpContextIds() {
		return null;
	}

	
	public Object getId() {
		return null;
	}

	
	public ILabelProvider getLabelProvider() {
		return null;
	}

	
	public boolean isCompatibleWith(IPropertyDescriptor anotherProperty) {
		return false;
	}

	
	public IValueParser getValueParser() {
		return null;
	}

	
	public Object getFieldValue(Object bean) {
		return null;
	}

	
	public void setFieldValue(Object bean, Object newValue) {
	}

	
	
	protected Class getObjectClass() {
		return getClass();
	}

	
	
	public Class getPropertyType() {
		return getClass();
	}

	
	public boolean isPropertyResettable(IStructuredSelection bean) {
		return false;
	}

	
	public String getSetName() {
		return null;
	}
}

