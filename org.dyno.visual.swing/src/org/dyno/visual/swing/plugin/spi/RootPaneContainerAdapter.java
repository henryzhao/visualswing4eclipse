package org.dyno.visual.swing.plugin.spi;


public abstract class RootPaneContainerAdapter extends CompositeAdapter {
	
	public RootPaneContainerAdapter() {
	}

	public RootPaneContainerAdapter(String name) {
		super(name);
	}

	public abstract WidgetAdapter getContentAdapter();
}
