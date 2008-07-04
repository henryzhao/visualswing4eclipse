/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.borders;

import java.awt.Insets;
import java.lang.reflect.Field;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.border.Border;

import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

abstract class InsetsProperty implements IWidgetPropertyDescriptor {
	private Field top;
	private Field left;
	private Field bottom;
	private Field right;

	public InsetsProperty() {
		try {
			top = getField("top");
			top.setAccessible(true);
			left = getField("left");
			left.setAccessible(true);
			bottom = getField("bottom");
			bottom.setAccessible(true);
			right = getField("right");
			right.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Field getField(String fieldName) {
		return getField(getBorderClass(), fieldName);
	}

	@SuppressWarnings("unchecked")
	protected abstract Class getBorderClass();

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

	@Override
	public Object getPropertyValue(Object bean) {
		Border border = (Border) bean;
		return border.getBorderInsets(null);
	}

	@Override
	public void setPropertyValue(Object bean, Object value) {
		try {
			Insets insets = (Insets) value;
			if (value == null)
				insets = new Insets(0, 0, 0, 0);
			top.set(bean, insets.top);
			left.set(bean, insets.left);
			bottom.set(bean, insets.bottom);
			right.set(bean, insets.right);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getId() {
		return "matte_border_insets";
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new TextCellEditor(parent) {
			@Override
			protected Object doGetValue() {
				String strInsets = (String) super.doGetValue();
				return decodeValue(strInsets);
			}

			@Override
			protected void doSetValue(Object value) {
				super.doSetValue(encodeValue(value));
			}
		};
		editor.setValidator(new InsetsCellEditorValidator());
		return editor;
	}

	private Object decodeValue(Object value) {
		if (value == null)
			return null;
		else if (value.equals("null")) {
			return null;
		} else {
			String sValue = value.toString().trim();
			sValue = sValue.substring(1, sValue.length() - 1);
			StringTokenizer tokenizer = new StringTokenizer(sValue, ",");
			String sTop = tokenizer.nextToken().trim();
			String sLeft = tokenizer.nextToken().trim();
			String sBottom = tokenizer.nextToken().trim();
			String sRight = tokenizer.nextToken().trim();
			int top = 0;
			int left = 0;
			int bottom = 0;
			int right = 0;
			try {
				top = Integer.parseInt(sTop);
				left = Integer.parseInt(sLeft);
				bottom = Integer.parseInt(sBottom);
				right = Integer.parseInt(sRight);
			} catch (NumberFormatException nfe) {
			}
			return new Insets(top, left, bottom, right);
		}
	}

	private Object encodeValue(Object value) {
		if (value == null)
			return "null";
		else {
			Insets insets = (Insets) value;
			return "(" + insets.top + ", " + insets.left + ", " + insets.bottom + ", " + insets.right + ")";
		}
	}
	@Override
	public boolean cloneProperty(Object bean, JComponent clone) {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(IConfigurationElement config, Class beanClass) {
	}

	@Override
	public boolean isGencode() {
		return false;
	}
	public String getSetCode(Object widget, ImportRewrite imports){
		return null;
	}

	@Override
	public boolean isPropertyResettable(Object bean) {
		return false;
	}

	@Override
	public boolean isPropertySet(String lnf, Object bean) {
		return false;
	}

	@Override
	public void resetPropertyValue(String lnfClassname, Object bean) {
	}

	@Override
	public void setBean(Object bean) {
	}

	@Override
	public void setCategory(String categoryName) {
	}

	@Override
	public void setFilterFlags(String[] filters) {
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
		return "insets";
	}

	@Override
	public String[] getFilterFlags() {
		return null;
	}

	@Override
	public Object getHelpContextIds() {
		return null;
	}

	private InsetsLabelProvider labelProvider;

	@Override
	public ILabelProvider getLabelProvider() {
		if (labelProvider == null) {
			labelProvider = new InsetsLabelProvider();
		}
		return labelProvider;
	}

	@Override
	public boolean isCompatibleWith(IPropertyDescriptor anotherProperty) {
		return false;
	}
}
