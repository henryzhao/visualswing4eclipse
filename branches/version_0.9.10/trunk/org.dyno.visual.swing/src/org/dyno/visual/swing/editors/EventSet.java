/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.editors;

import java.beans.EventSetDescriptor;
import java.util.List;
/**
 * 
 * EventSet
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
class EventSet {
	private List<EventMethod> methods;
	private String name;
	private EventDesc parent;
	private EventSetDescriptor eventSet;
	public EventSet(EventSetDescriptor eventSet, String name, EventDesc parent){
		this.eventSet = eventSet;
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
	public EventSetDescriptor getEventSet(){
		return eventSet;
	}
}

