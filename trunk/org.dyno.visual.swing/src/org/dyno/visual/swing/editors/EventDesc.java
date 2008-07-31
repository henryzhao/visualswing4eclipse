/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.editors;

import java.awt.Component;
import java.util.List;
/**
 * 
 * EventDesc
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
class EventDesc {
	private List<EventSet> eventSets;
	private Component widget;
	public EventDesc(Component widget){
		this.widget = widget;
	}
	public Component getWidget(){
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
