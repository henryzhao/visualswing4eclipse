package org.dyno.visual.swing.plugin.spi;


public class WidgetEvent {
	private WidgetAdapter parent;
	private WidgetAdapter target;
	private WidgetAdapter destinationParent;
	public WidgetEvent(WidgetAdapter parent, WidgetAdapter target){
		this.parent = parent;
		this.target = target;
	}
	public WidgetEvent(WidgetAdapter src, WidgetAdapter dest, WidgetAdapter target){
		this.parent = src;
		this.destinationParent = dest;
		this.target = target;
	}
	public WidgetAdapter getParent(){
		return parent;
	}
	public WidgetAdapter getDestinationParent(){
		return destinationParent;
	}
	public WidgetAdapter getTarget(){
		return target;
	}
}
