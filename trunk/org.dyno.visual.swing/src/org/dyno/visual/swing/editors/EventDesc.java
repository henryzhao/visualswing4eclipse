package org.dyno.visual.swing.editors;

import java.util.List;

import javax.swing.JComponent;

class EventDesc {
	private List<EventSet> eventSets;
	private JComponent widget;
	public EventDesc(JComponent widget){
		this.widget = widget;
	}
	public JComponent getWidget(){
		return widget;
	}
	public String toString(){
		return "Events";
	}
	public List<EventSet> getEventSets() {
		return eventSets;
	}
	public void setEventSets(List<EventSet> eventSets) {
		this.eventSets = eventSets;
	}
}
