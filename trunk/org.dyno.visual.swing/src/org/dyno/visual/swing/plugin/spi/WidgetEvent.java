package org.dyno.visual.swing.plugin.spi;


public class WidgetEvent {
	private CompositeAdapter parent;
	private WidgetAdapter target;
	private CompositeAdapter destinationParent;
	public WidgetEvent(CompositeAdapter parent, WidgetAdapter target){
		this.parent = parent;
		this.target = target;
	}
	public WidgetEvent(CompositeAdapter src, CompositeAdapter dest, WidgetAdapter target){
		this.parent = src;
		this.destinationParent = dest;
		this.target = target;
	}
	public CompositeAdapter getParent(){
		return parent;
	}
	public CompositeAdapter getDestinationParent(){
		return destinationParent;
	}
	public WidgetAdapter getTarget(){
		return target;
	}
}
