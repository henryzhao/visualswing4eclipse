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
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import javax.swing.UIManager;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;
import org.dyno.visual.swing.plugin.spi.IValueParser;
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
public class WidgetProperty extends PropertyAdapter{
	
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
	protected Object default_value;

	public WidgetProperty(String name, Class beanClass, ILabelProviderFactory label, ICellEditorFactory editor, Object defaultValue) {
		this.beanClass = beanClass;
		this.propertyName = name;
		try {
			propertyDescriptor = new PropertyDescriptor(propertyName, beanClass);
		} catch (IntrospectionException e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		gencode = true;
		displayName = name + TEXT_TRAILING;
		editable = true;
		labelFactory = label;
		editorFactory = editor;
		this.default_value = defaultValue;
	}

	public WidgetProperty() {

	}

	public PropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}

	public WidgetProperty(String name, Class beanClass, Object defaultValue) {
		this.beanClass = beanClass;
		this.propertyName = name;
		try {
			propertyDescriptor = new PropertyDescriptor(propertyName, beanClass);
		} catch (IntrospectionException e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		gencode = true;
		displayName = name + TEXT_TRAILING;
		editable = true;
		Class<?> type = propertyDescriptor.getPropertyType();
		TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
		if (ta != null) {
			labelFactory = ta.getRenderer();
			editorFactory = ta.getEditor();
		}
		this.default_value = defaultValue;
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
				propertyDescriptor = new PropertyDescriptor(propertyName, beanClass);
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
		displayName = displayName + TEXT_TRAILING;
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
		String sParser = config.getAttribute("parser");
		if (sParser != null && sParser.trim().length() > 0) {
			try {
				parser = (IValueParser) config.createExecutableExtension("parser");
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
			editorFactory = (ICellEditorFactory) config.createExecutableExtension("editor");
		} catch (CoreException e) {
			VisualSwingPlugin.getLogger().error(e);
		}
	}

	private void createLabelProviderFactory(IConfigurationElement config) {
		try {
			labelFactory = (ILabelProviderFactory) config.createExecutableExtension("renderer");
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
	protected Object getDefaultValue(Object b, String lnfClassname){
		if (b instanceof Component&&Component.class.isAssignableFrom(beanClass)) {
			WidgetAdapter wa = WidgetAdapter.getWidgetAdapter((Component) b);
			if (wa.isRoot() && b.getClass().getSuperclass() == beanClass || !wa.isRoot() && b.getClass() == beanClass) {
				ILookAndFeelAdapter adapter = null;
				if (lnfClassname != null)
					adapter = ExtensionRegistry.getLnfAdapter(lnfClassname);
				else
					adapter = ExtensionRegistry.getLnfAdapter(UIManager.getCrossPlatformLookAndFeelClassName());
				if (adapter != null)
				default_value = adapter.getDefaultValue(beanClass, propertyName);
			} else{ 
				Class compClass;
				if(wa.isRoot()&&b.getClass().getSuperclass()!=beanClass)
					compClass=b.getClass().getSuperclass();
				else if(!wa.isRoot()&&b.getClass()!=beanClass)
					compClass=b.getClass();
				else if(wa.isRoot()&&b.getClass()!=beanClass&&b.getClass().getSuperclass()!=beanClass)
				    compClass=b.getClass().getSuperclass();
				else if(!wa.isRoot()&&b.getClass()!=beanClass&&b.getClass().getSuperclass()!=beanClass)
					compClass=b.getClass();
				else
					compClass=null;
				if(compClass!=null){
					Object obj=DEFAULT_OBJECTS.get(compClass);
					if(obj==null){
						try{
						obj=compClass.newInstance();
						}catch(Exception e){
							VisualSwingPlugin.getLogger().error(e);
							obj=null;
						}
						DEFAULT_OBJECTS.put(compClass, obj);
					}
					default_value=getFieldValue(obj);
				}
			}
		}
		return default_value;
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
			TypeAdapter adapter = ExtensionRegistry.getTypeAdapter(value.getClass());
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
		Map<String, Boolean> editedMap = adapter.getEditingMap();
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
	protected Class getObjectClass() {
		return getClass();
	}

	public ICodeGen getCodeGenerator() {
		Class typeClass = propertyDescriptor.getPropertyType();
		TypeAdapter typeAdapter = ExtensionRegistry.getTypeAdapter(typeClass);
		if (editorFactory != null && editorFactory instanceof ItemProviderCellEditorFactory) {
			return editorFactory;
		} else if (typeAdapter != null) {
			return typeAdapter.getCodegen();
		} else {
			return editorFactory;
		}
	}
	@Override
	public String getSetName() {
		return propertyDescriptor.getWriteMethod().getName();
	}
}
