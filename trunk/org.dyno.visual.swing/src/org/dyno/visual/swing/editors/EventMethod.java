package org.dyno.visual.swing.editors;

class EventMethod {
	private String name;
	private EventSet parent;
	public EventMethod(String name, EventSet eSet){
		this.name = name;
		this.parent = eSet;
	}
	public EventSet getParent(){
		return parent;
	}
	public String toString(){
		return this.name;
	}
}
