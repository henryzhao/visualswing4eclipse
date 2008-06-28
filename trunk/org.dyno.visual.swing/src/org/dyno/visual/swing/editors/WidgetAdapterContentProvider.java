package org.dyno.visual.swing.editors;

import java.awt.LayoutManager;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.PropertySource2;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

public class WidgetAdapterContentProvider implements IPropertySourceProvider {
	private static IPropertySourceProvider getProvider(Object object) {
		if (object instanceof JComponent) {
			JComponent widget = (JComponent) object;
			return WidgetAdapter.getWidgetAdapter(widget);
		} else if (object instanceof LayoutManager) {
			LayoutAdapter adapter = LayoutAdapter.createLayoutAdapter(object.getClass());
			return adapter;
		}  
		return null;
	}

	@Override
	public IPropertySource getPropertySource(Object object) {
		if (object == null)
			return new PropertySource2(null, new IWidgetPropertyDescriptor[0]);
		IPropertySourceProvider provider = getProvider(object);
		if (provider != null) {
			return provider.getPropertySource(object);
		} else
			return null;
	}
}
