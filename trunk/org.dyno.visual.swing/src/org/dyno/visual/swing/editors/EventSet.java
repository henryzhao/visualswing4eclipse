package org.dyno.visual.swing.editors;

import java.util.List;

class EventSet {
	private List<EventMethod> methods;
	private String name;
	private EventDesc parent;
	public EventSet(String name, EventDesc parent){
		this.name = name;
		this.parent = parent;
	}
	public EventDesc getParent(){
		return parent;
	}
	public String toString(){
		return name;
	}
	public List<EventMethod> getMethods() {
		return methods;
	}
	public void setMethods(List<EventMethod> methods) {
		this.methods = methods;
	}
}
