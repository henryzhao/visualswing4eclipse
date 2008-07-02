package org.dyno.visual.swing.plugin.spi;

import javax.swing.JComponent;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public interface IWidgetPropertyDescriptor extends IPropertyDescriptor{
    boolean isPropertyResettable(Object bean);
    boolean isPropertySet(String lnfClassname, Object bean);
	Object getPropertyValue(Object bean);
	void resetPropertyValue(Object bean);
	void setPropertyValue(Object bean, Object value);
	boolean cloneProperty(Object bean, JComponent clone);
	String getSetCode(Object widget, ImportRewrite imports);
	boolean isGencode();
	void setBean(Object bean);
	void setFilterFlags(String[]filters);
	void setCategory(String categoryName);
	@SuppressWarnings("unchecked")
	void init(IConfigurationElement config, Class beanClass);
}
