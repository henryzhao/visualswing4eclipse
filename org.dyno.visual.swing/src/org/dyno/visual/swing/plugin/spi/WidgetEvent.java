package org.dyno.visual.swing.plugin.spi;


public class WidgetEvent {
	private CompositeAdapter parent;
	private WidgetAdapter target;
	public WidgetEvent(CompositeAdapter parent, WidgetAdapter target){
		this.parent = parent;
		this.target = target;
	}
	public CompositeAdapter getParent(){
		return parent;
	}
	public WidgetAdapter getTarget(){
		return target;
	}
}
