package org.dyno.visual.swing.plugin.spi;

import java.awt.Component;

public class PropertyEvent {
	private IWidgetPropertyDescriptor property;
	private Component component;
	public PropertyEvent(Component component, IWidgetPropertyDescriptor property){
		this.component = component;
		this.property = property;
	}
	public IWidgetPropertyDescriptor getProperty(){
		return property;
	}
	public Component getComponent(){
		return component;
	}
}
