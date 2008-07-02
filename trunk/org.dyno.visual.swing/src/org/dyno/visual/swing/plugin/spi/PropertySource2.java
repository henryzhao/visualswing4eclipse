package org.dyno.visual.swing.plugin.spi;

import java.util.HashMap;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;

public class PropertySource2 implements IPropertySource2 {
	private Object bean;
	private IWidgetPropertyDescriptor[] properties;
	private HashMap<Object, IWidgetPropertyDescriptor> propertyMap;
	private String lnfClassname;
	public PropertySource2(Object bean, IWidgetPropertyDescriptor[] properties) {
		this.bean = bean;
		this.properties = properties;
		this.propertyMap = new HashMap<Object, IWidgetPropertyDescriptor>();
		for (IWidgetPropertyDescriptor property : properties) {
			String id = (String) property.getId();
			this.propertyMap.put(id, property);
			property.setBean(bean);
		}
		LookAndFeel lnf = UIManager.getLookAndFeel();
		if (lnf == null)
			lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
		else
			lnfClassname = lnf.getClass().getName();
	}
	public PropertySource2(String lnfClassname, Object bean, IWidgetPropertyDescriptor[] properties){
		this.lnfClassname = lnfClassname;
		this.bean = bean;
		this.properties = properties;
		this.propertyMap = new HashMap<Object, IWidgetPropertyDescriptor>();
		for (IWidgetPropertyDescriptor property : properties) {
			String id = (String) property.getId();
			this.propertyMap.put(id, property);
			property.setBean(bean);
		}
	}
	@Override
	public boolean isPropertyResettable(Object id) {
		IWidgetPropertyDescriptor property = propertyMap.get(id);
		return property != null && property.isPropertyResettable(bean);
	}

	@Override
	public boolean isPropertySet(Object id) {
		IWidgetPropertyDescriptor property = propertyMap.get(id);
		return property != null && property.isPropertySet(lnfClassname, bean);
	}

	@Override
	public Object getEditableValue() {
		return bean;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return properties;
	}

	@Override
	public Object getPropertyValue(Object id) {
		IWidgetPropertyDescriptor property = propertyMap.get(id);
		return property == null ? null : property.getPropertyValue(bean);
	}

	@Override
	public void resetPropertyValue(Object id) {
		IWidgetPropertyDescriptor property = propertyMap.get(id);
		if (property != null)
			property.resetPropertyValue(bean);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		IWidgetPropertyDescriptor property = propertyMap.get(id);
		if (property != null)
			property.setPropertyValue(bean, value);
	}
}
