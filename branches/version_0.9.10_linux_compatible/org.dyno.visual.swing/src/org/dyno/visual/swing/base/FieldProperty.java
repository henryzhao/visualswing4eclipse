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
import java.lang.reflect.Field;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.dyno.visual.swing.plugin.spi.IValueParser;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
/**
 * 
 * FieldProperty
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
@SuppressWarnings("unchecked")
public class FieldProperty extends PropertyAdapter{
	private Object lastValue;
	private Object default_value;

	private ICellEditorFactory editorFactory;
	private ILabelProviderFactory labelFactory;
	private Field field;
	private String id;
	private String displayName;

	private IStructuredSelection bean;

	public FieldProperty(String id, String name, Class beanClass) {
		this.id = id;
		try {
			field = getField(beanClass, name);
			field.setAccessible(true);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		displayName = name;
		Class<?> type = field.getType();
		TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
		if (ta != null) {
			labelFactory = ta.getRenderer();
			editorFactory = ta.getEditor();
		}
	}

	@Override
	public void init(IConfigurationElement config, Class beanClass) {
	}
	
	public FieldProperty(String id, String name, Class beanClass, ILabelProviderFactory label, ICellEditorFactory editor) {
		this.id = id;
		try {
			field = getField(beanClass, name);
			field.setAccessible(true);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		displayName = name;
		labelFactory = label;
		editorFactory = editor;
	}

	
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

	public void setBean(IStructuredSelection bean) {
		this.bean = bean;
	}

	
	@Override
	public Object getPropertyValue(IStructuredSelection bean) {
		assert !bean.isEmpty();
		try {
			Object value = field.get(bean.getFirstElement());
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
			return field.get(bean);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
		}
		return null;
	}

	@Override
	public void setFieldValue(Object bean, Object newValue) {
	}
	@Override
	public boolean isPropertyResettable(IStructuredSelection bean) {
		return true;
	}
	@Override
	protected String getPropertyName(){
		return field.getName();
	}

	@Override
	protected Object getDefaultValue(Object b, String lnfClassname) {
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
					field.set(b, value);
					if (b instanceof Component) {
						Component jcomp = (Component) b;
						WidgetAdapter adapter = WidgetAdapter
								.getWidgetAdapter(jcomp);
						if (adapter != null) {
							adapter.repaintDesigner();
						}
					}
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
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return displayName+TEXT_TRAILING;
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
	public boolean cloneProperty(Object bean, Component clone) {
		try {
			Object value = field.get(bean);
			field.set(clone, value);
			return true;
		} catch (Exception e) {
			return false;
		}
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

	@Override
	public boolean isEdited(WidgetAdapter adapter) {
		return false;
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
		return field.getType();
	}

	public String getFieldName() {
		return field.getName();
	}
}

