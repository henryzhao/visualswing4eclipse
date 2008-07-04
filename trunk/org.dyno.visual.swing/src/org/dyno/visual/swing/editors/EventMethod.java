/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.editors;
/**
 * 
 * EventMethod
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
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
