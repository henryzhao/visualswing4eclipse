package org.dyno.visual.swing.plugin.spi;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.RootPaneContainer;


public abstract class RootPaneContainerAdapter extends CompositeAdapter {

	public RootPaneContainerAdapter(String name) {
		super(name);
	}
	@Override
	public Component getContentArea(){
		RootPaneContainer rootPaneContainer = (RootPaneContainer) getWidget();
		return rootPaneContainer.getRootPane();
	}

	public abstract WidgetAdapter getContentAdapter();
	
	public Component getContentPane(){
		return ((RootPaneContainer)getWidget()).getContentPane();
	}

	@Override
	public Class<?> getDefaultLayout() {
		return BorderLayout.class;
	}	
}
