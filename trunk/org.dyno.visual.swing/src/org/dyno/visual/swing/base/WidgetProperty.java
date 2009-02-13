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
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;

import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.AbstractAdaptable;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;
import org.dyno.visual.swing.plugin.spi.ISystemValue;
import org.dyno.visual.swing.plugin.spi.IValueParser;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.undo.SetValueOperation;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

/**
 * 
 * WidgetProperty
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
@SuppressWarnings("unchecked")
public class WidgetProperty extends AbstractAdaptable implements
		IWidgetPropertyDescriptor {
	protected Object lastValue;

	protected String category;
	protected ICellEditorFactory editorFactory;
	protected ILabelProviderFactory labelFactory;
	protected boolean editable;
	protected PropertyDescriptor propertyDescriptor;
	protected String helpContextId;
	protected String description;
	protected String propertyName;
	protected String displayName;
	protected String[] filters;

	
	protected Class beanClass;
	protected boolean gencode;
	protected IValueParser parser;

	
	public WidgetProperty(String name, Class beanClass,
			ILabelProviderFactory label, ICellEditorFactory editor) {
		this.beanClass = beanClass;
		this.propertyName = name;
		try {
			propertyDescriptor = new PropertyDescriptor(propertyName, beanClass);
		} catch (IntrospectionException e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		gencode = true;
		displayName = name;
		editable = true;
		labelFactory = label;
		editorFactory = editor;
	}

	public WidgetProperty() {

	}

	public PropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}

	
	public WidgetProperty(String name, Class beanClass) {
		this.beanClass = beanClass;
		this.propertyName = name;
		try {
			propertyDescriptor = new PropertyDescriptor(propertyName, beanClass);
		} catch (IntrospectionException e) {
			VisualSwingPlugin.getLogger().error(e);
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

	
	public WidgetProperty(IConfigurationElement config, Class beanClass) {
		init(config, beanClass);
	}

	
	@Override
	public void init(IConfigurationElement config, Class beanClass) {
		this.beanClass = beanClass;
		propertyName = config.getAttribute("name");
		if (propertyDescriptor == null) {
			try {
				propertyDescriptor = new PropertyDescriptor(propertyName,
						beanClass);
			} catch (IntrospectionException e) {
				VisualSwingPlugin.getLogger().error(e);
			}
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
		editable = sEditable == null || sEditable.trim().length() == 0
				|| sEditable.toLowerCase().equals("true");
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
		String sParser = config.getAttribute("parser");
		if (sParser != null && sParser.trim().length() > 0) {
			try {
				parser = (IValueParser) config
						.createExecutableExtension("parser");
			} catch (CoreException e) {
				VisualSwingPlugin.getLogger().error(e);
			}
		} else if (ta != null) {
			parser = ta.getParser();
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
			editorFactory = (ICellEditorFactory) config
					.createExecutableExtension("editor");
		} catch (CoreException e) {
			VisualSwingPlugin.getLogger().error(e);
		}
	}

	private void createLabelProviderFactory(IConfigurationElement config) {
		try {
			labelFactory = (ILabelProviderFactory) config
					.createExecutableExtension("renderer");
		} catch (CoreException e) {
			VisualSwingPlugin.getLogger().error(e);
		}
	}

	public void setCategory(String category) {
		this.category = category;
	}

	private IStructuredSelection bean;

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
	public boolean isPropertyResettable(IStructuredSelection bean) {
		return true;
	}

	
	@Override
	public boolean isPropertySet(String lnfClassname, IStructuredSelection bean) {
 		assert !bean.isEmpty();
		Object b = bean.getFirstElement();
		if (propertyName.equals("preferredSize") && b instanceof Component) {
			return ((Component) b).isPreferredSizeSet();
		} else if (propertyName.equals("minimumSize") && b instanceof Component)
			return ((Component) b).isMinimumSizeSet();
		else if (propertyName.equals("maximumSize") && b instanceof Component)
			return ((Component) b).isMaximumSizeSet();
		else if(propertyName.equals("layout")&&b instanceof Container){
			CompositeAdapter a=(CompositeAdapter) WidgetAdapter.getWidgetAdapter((Component)b);
			if(a.getLayoutAdapter().isDefaultLayout()){
				return false;
			}else
				return true;			
		}
		Object value = getFieldValue(b);
		if (value != null && value instanceof UIResource)
			return false;
		Class<?> propertyType = propertyDescriptor.getPropertyType();

		ILookAndFeelAdapter adapter = null;
		if (lnfClassname != null)
			adapter = ExtensionRegistry.getLnfAdapter(lnfClassname);
		else
			adapter = ExtensionRegistry.getLnfAdapter(UIManager
					.getCrossPlatformLookAndFeelClassName());
		if (adapter == null)
			return false;
		Object default_value = adapter.getDefaultValue(beanClass, propertyName);
		if (default_value instanceof ISystemValue)
			return true;
		if (propertyType == byte.class) {
			byte bv = value == null ? 0 : ((Byte) value).byteValue();
			byte dv = default_value == null ? 0 : ((Byte) default_value)
					.byteValue();
			return bv != dv;
		} else if (propertyType == char.class) {
			char bv = value == null ? 0 : ((Character) value).charValue();
			char dv = default_value == null ? 0 : ((Character) default_value)
					.charValue();
			return bv != dv;
		} else if (propertyType == short.class) {
			short bv = value == null ? 0 : ((Short) value).shortValue();
			short dv = default_value == null ? 0 : ((Short) default_value)
					.shortValue();
			return bv != dv;
		} else if (propertyType == int.class) {
			int bv = value == null ? 0 : ((Integer) value).intValue();
			int dv = default_value == null ? 0 : ((Integer) default_value)
					.intValue();
			return bv != dv;
		} else if (propertyType == long.class) {
			long bv = value == null ? 0 : ((Long) value).longValue();
			long dv = default_value == null ? 0 : ((Long) default_value)
					.longValue();
			return bv != dv;
		} else if (propertyType == float.class) {
			float bv = value == null ? 0 : ((Float) value).floatValue();
			float dv = default_value == null ? 0 : ((Float) default_value)
					.floatValue();
			return bv != dv;
		} else if (propertyType == double.class) {
			double bv = value == null ? 0 : ((Double) value).doubleValue();
			double dv = default_value == null ? 0 : ((Double) default_value)
					.doubleValue();
			return bv != dv;
		} else if (propertyType == void.class) {
			return false;
		} else if (propertyType == boolean.class) {
			boolean bv = value == null ? false : ((Boolean) value)
					.booleanValue();
			boolean dv = default_value == null ? false
					: ((Boolean) default_value).booleanValue();
			return bv != dv;
		} else if (propertyType == Byte.class) {
			byte bv = value == null ? 0 : ((Byte) value).byteValue();
			byte dv = default_value == null ? 0 : ((Byte) default_value)
					.byteValue();
			return bv != dv;
		} else if (propertyType == Character.class) {
			char bv = value == null ? 0 : ((Character) value).charValue();
			char dv = default_value == null ? 0 : ((Character) default_value)
					.charValue();
			return bv != dv;
		} else if (propertyType == Short.class) {
			short bv = value == null ? 0 : ((Short) value).shortValue();
			short dv = default_value == null ? 0 : ((Short) default_value)
					.shortValue();
			return bv != dv;
		} else if (propertyType == Integer.class) {
			int bv = value == null ? 0 : ((Integer) value).intValue();
			int dv = default_value == null ? 0 : ((Integer) default_value)
					.intValue();
			return bv != dv;
		} else if (propertyType == Long.class) {
			long bv = value == null ? 0 : ((Long) value).longValue();
			long dv = default_value == null ? 0 : ((Long) default_value)
					.longValue();
			return bv != dv;
		} else if (propertyType == Float.class) {
			float bv = value == null ? 0 : ((Float) value).floatValue();
			float dv = default_value == null ? 0 : ((Float) default_value)
					.floatValue();
			return bv != dv;
		} else if (propertyType == Double.class) {
			double bv = value == null ? 0 : ((Double) value).doubleValue();
			double dv = default_value == null ? 0 : ((Double) default_value)
					.doubleValue();
			return bv != dv;
		} else if (propertyType == Void.class) {
			return false;
		} else if (propertyType == Boolean.class) {
			boolean bv = value == null ? false : ((Boolean) value)
					.booleanValue();
			boolean dv = default_value == null ? false
					: ((Boolean) default_value).booleanValue();
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
				TypeAdapter typeAdapter = ExtensionRegistry
						.getTypeAdapter(propertyType);
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
	public void resetPropertyValue(String lnfClassname,
			IStructuredSelection bean) {
		assert !bean.isEmpty();
		Object b = bean.getFirstElement();
		if (propertyName.equals("preferredSize") && b instanceof Component) {
			return;
		} else if (propertyName.equals("minimumSize") && b instanceof Component)
			return;
		else if (propertyName.equals("maximumSize") && b instanceof Component)
			return;
		Object value = getFieldValue(b);
		if (value != null && value instanceof UIResource)
			return;
		Class<?> propertyType = propertyDescriptor.getPropertyType();
		ILookAndFeelAdapter adapter = ExtensionRegistry
				.getLnfAdapter(lnfClassname);
		if (adapter == null)
			return;
		Object default_value = adapter.getDefaultValue(beanClass, propertyName);
		if (propertyType == byte.class) {
			byte dv = default_value == null ? 0 : ((Byte) default_value)
					.byteValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == char.class) {
			char dv = default_value == null ? 0 : ((Character) default_value)
					.charValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == short.class) {
			short dv = default_value == null ? 0 : ((Short) default_value)
					.shortValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == int.class) {
			int dv = default_value == null ? 0 : ((Integer) default_value)
					.intValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == long.class) {
			long dv = default_value == null ? 0 : ((Long) default_value)
					.longValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == float.class) {
			float dv = default_value == null ? 0 : ((Float) default_value)
					.floatValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == double.class) {
			double dv = default_value == null ? 0 : ((Double) default_value)
					.doubleValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == void.class) {
		} else if (propertyType == boolean.class) {
			boolean dv = default_value == null ? false
					: ((Boolean) default_value).booleanValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == Byte.class) {
			byte dv = default_value == null ? 0 : ((Byte) default_value)
					.byteValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == Character.class) {
			char dv = default_value == null ? 0 : ((Character) default_value)
					.charValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == Short.class) {
			short dv = default_value == null ? 0 : ((Short) default_value)
					.shortValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == Integer.class) {
			int dv = default_value == null ? 0 : ((Integer) default_value)
					.intValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == Long.class) {
			long dv = default_value == null ? 0 : ((Long) default_value)
					.longValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == Float.class) {
			float dv = default_value == null ? 0 : ((Float) default_value)
					.floatValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == Double.class) {
			double dv = default_value == null ? 0 : ((Double) default_value)
					.doubleValue();
			setPropertyValue(bean, dv);
		} else if (propertyType == Void.class) {
		} else if (propertyType == Boolean.class) {
			boolean dv = default_value == null ? false
					: ((Boolean) default_value).booleanValue();
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
					IOperationHistory operationHistory = PlatformUI
							.getWorkbench().getOperationSupport()
							.getOperationHistory();
					if (b instanceof Component) {
						Component jcomp = (Component) b;
						WidgetAdapter adapter = WidgetAdapter
								.getWidgetAdapter(jcomp);
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
		return propertyName;
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
		Object value = getFieldValue(bean);
		if (value != null) {
			TypeAdapter adapter = ExtensionRegistry.getTypeAdapter(value
					.getClass());
			if (adapter != null && adapter.getCloner() != null) {
				value = adapter.getCloner().clone(value);
			}
		}
		setFieldValue(clone, value);
		return true;
	}
	
	@Override
	public boolean isGencode() {
		return gencode;
	}

	@Override
	public boolean isEdited(WidgetAdapter adapter) {
		Map<String, Boolean> editedMap=adapter.getEditingMap();
		Boolean bool = editedMap.get(propertyName);
		return bool == null ? false : bool.booleanValue();
	}

	@Override
	public IValueParser getValueParser() {
		return parser;
	}

	
	@Override
	public void setFieldValue(Object bean, Object newValue) {
		try {
			Method writeMethod = propertyDescriptor.getWriteMethod();
			if (writeMethod != null) {
				writeMethod.invoke(bean, newValue);
			} else {
				Class clazz = bean.getClass();
				Field field = clazz.getField(propertyName);
				field.setAccessible(true);
				field.set(bean, newValue);
			}
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
	}

	
	@Override
	public Object getFieldValue(Object bean) {
		try {
			if (propertyDescriptor.getReadMethod() != null) {
				return propertyDescriptor.getReadMethod().invoke(bean);
			} else {
				Class clazz = bean.getClass();
				Field field = clazz.getField(propertyName);
				field.setAccessible(true);
				return field.get(bean);
			}
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
			return null;
		}
	}

	
	@Override
	public Class getPropertyType() {
		return propertyDescriptor.getPropertyType();
	}
	
	@Override
	protected Class getObjectClass(){
		return getClass();
	}

	public ICodeGen getCodeGenerator() {
		Class typeClass = propertyDescriptor.getPropertyType();
		TypeAdapter typeAdapter = ExtensionRegistry.getTypeAdapter(typeClass);
		if (editorFactory != null
				&& editorFactory instanceof ItemProviderCellEditorFactory) {
			return editorFactory;
		} else if (typeAdapter != null) {
			return typeAdapter.getCodegen();
		} else {
			return editorFactory;
		}
	}

	public String getSetName() {
		return propertyDescriptor.getWriteMethod().getName();		
	}
}
