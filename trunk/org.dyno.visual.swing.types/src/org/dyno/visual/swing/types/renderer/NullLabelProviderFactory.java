package org.dyno.visual.swing.types.renderer;

import org.dyno.visual.swing.plugin.spi.ILabelProviderFactory;
import org.eclipse.jface.viewers.ILabelProvider;

public class NullLabelProviderFactory implements ILabelProviderFactory {
	public ILabelProvider getLabelProvider() {
		return null;
	}

}
