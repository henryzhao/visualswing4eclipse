package org.dyno.visual.swing.base;

import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.eclipse.jface.viewers.ILabelProvider;

public class FactoryRenderer implements ILabelProviderFactory {
	private static final long serialVersionUID = -4403435758517308113L;
	private FactoryLabelProvider labelProvider;
	private IFactoryProvider provider;
	public FactoryRenderer(IFactoryProvider provider){
		this.provider = provider;
	}
	@Override
	public ILabelProvider getLabelProvider() {
		if (labelProvider == null)
			labelProvider = new FactoryLabelProvider(provider);
		return labelProvider;
	}
}
