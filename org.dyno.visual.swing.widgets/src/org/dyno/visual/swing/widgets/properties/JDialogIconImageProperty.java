package org.dyno.visual.swing.widgets.properties;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import javax.swing.JDialog;

import org.dyno.visual.swing.base.WidgetProperty;
import org.dyno.visual.swing.widgets.WidgetPlugin;
import org.eclipse.core.runtime.IConfigurationElement;

@SuppressWarnings("unchecked")
public class JDialogIconImageProperty extends WidgetProperty {

	
	public void init(IConfigurationElement config, Class beanClass) {
		try {
			propertyDescriptor = new JDialogIconImagePropertyDescriptor();
		} catch (IntrospectionException e) {
			WidgetPlugin.getLogger().error(e);
		}
		super.init(config, beanClass);
	}	
	private class JDialogIconImagePropertyDescriptor extends PropertyDescriptor{
		private JDialogIconImagePropertyDescriptor() throws IntrospectionException{
			super("iconImage", JDialog.class, null, "setIconImage");
		}
	}
	
	public Object getFieldValue(Object bean) {
		return null;
	}
}
