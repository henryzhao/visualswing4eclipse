package org.dyno.visual.swing.plugin.spi;

import java.util.HashMap;
import java.util.Map;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

public abstract class AdaptableAdapter implements IAdapter {

	protected Map<String, Object> adapters = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapterClass) {
		Object object = adapters.get(adapterClass.getName());
		if (object == null) {
			IConfigurationElement config = getConfig(adapterClass.getName(), getObjectClass());
			if (config == null)
				return null;
			try {
				object = config.createExecutableExtension("implementation");
				if (object == null)
					return null;
				if (object instanceof IAdapterContext) {
					((IAdapterContext) object).setAdapter(this);
				}
				adapters.put(adapterClass.getName(), object);
				return object;
			} catch (CoreException e) {
				VisualSwingPlugin.getLogger().error(e);
				return null;
			}
		} else
			return object;
	}

	@SuppressWarnings("unchecked")
	private IConfigurationElement getConfig(String adapter, Class type) {
		IConfigurationElement config = ExtensionRegistry.getAdapterConfig(
				adapter, type.getName());
		if (config == null) {
			if (type == Object.class)
				return null;
			else {
				config = getConfig(adapter, type.getSuperclass());
				if (config != null)
					return config;
				else {
					Class[] interfaces = type.getInterfaces();
					for (Class inter : interfaces) {
						config = getConfig(adapter, inter);
						if (config != null)
							return config;
					}
					return null;
				}
			}
		} else
			return config;
	}
}
