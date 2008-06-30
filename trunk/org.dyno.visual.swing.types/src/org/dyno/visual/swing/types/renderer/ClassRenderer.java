package org.dyno.visual.swing.types.renderer;

import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.eclipse.jface.viewers.ILabelProvider;

public class ClassRenderer implements ILabelProviderFactory {
	private ClassLabelProvider provider;
	@Override
	public ILabelProvider getLabelProvider() {
		if(provider==null)
			provider = new ClassLabelProvider();
		return provider;
	}
}