package org.dyno.visual.swing.types.renderer;

import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.eclipse.jface.viewers.ILabelProvider;

public class BorderRenderer implements ILabelProviderFactory {
	private BorderLabelProvider provider;
	@Override
	public ILabelProvider getLabelProvider() {
		if(provider==null)
			provider = new BorderLabelProvider();
		return provider;
	}
}