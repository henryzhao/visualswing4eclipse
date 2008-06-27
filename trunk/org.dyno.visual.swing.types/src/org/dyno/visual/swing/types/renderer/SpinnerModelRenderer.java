package org.dyno.visual.swing.types.renderer;

import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.eclipse.jface.viewers.ILabelProvider;

public class SpinnerModelRenderer implements ILabelProviderFactory {
	private SpinnerModelLabelProvider provider;
	@Override
	public ILabelProvider getLabelProvider() {
		if(provider==null)
			provider = new SpinnerModelLabelProvider();
		return provider;
	}
}