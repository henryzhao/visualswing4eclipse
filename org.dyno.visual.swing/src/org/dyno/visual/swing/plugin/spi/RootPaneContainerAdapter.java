package org.dyno.visual.swing.plugin.spi;

import java.awt.BorderLayout;


public abstract class RootPaneContainerAdapter extends CompositeAdapter {
//	
//	public RootPaneContainerAdapter() {
//	}

	public RootPaneContainerAdapter(String name) {
		super(name);
	}

	public abstract WidgetAdapter getContentAdapter();

	@Override
	public Class<?> getDefaultLayout() {
		return BorderLayout.class;
	}
	
}
