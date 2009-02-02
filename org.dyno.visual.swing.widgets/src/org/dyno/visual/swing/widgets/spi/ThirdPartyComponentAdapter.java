package org.dyno.visual.swing.widgets.spi;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.WidgetPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

@SuppressWarnings("unchecked")
public class ThirdPartyComponentAdapter extends WidgetAdapter {
	private Class beanClass;

	public ThirdPartyComponentAdapter() {
	}

	@Override
	protected Component createWidget() {
		if (beanClass != null) {
			try {
				Component comp = (Component) beanClass.newInstance();
				Dimension size = comp.getPreferredSize();
				comp.setSize(size);
				comp.doLayout();
				comp.validate();
				return comp;
			} catch (Exception e) {
				WidgetPlugin.getLogger().error(e);
			}
		}
		return null;
	}

	@Override
	protected Component newWidget() {
		if (beanClass != null) {
			try {
				return (Component) beanClass.newInstance();
			} catch (Exception e) {
				WidgetPlugin.getLogger().error(e);
			}
		}
		return null;
	}

	@Override
	public Class getWidgetClass() {
		return beanClass;
	}

	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		String widgetClassname=config.getAttribute("widgetClass");
		IContributor contrib = config.getContributor();
		String pluginID = contrib.getName();
		Bundle bundle = Platform.getBundle(pluginID);
		try {
			beanClass=bundle.loadClass(widgetClassname);
		} catch (Exception e) {
			WidgetPlugin.getLogger().error(e);
		}
		this.widget = createWidget();
		this.hotspotPoint = new Point(widget.getWidth() / 2, widget.getHeight() / 2);
		attach();		
		super.setInitializationData(config, propertyName, data);
	}
}
