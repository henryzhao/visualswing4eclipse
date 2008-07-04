/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.editors;

import java.util.List;

import javax.swing.JComponent;
/**
 * 
 * EventDesc
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
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
