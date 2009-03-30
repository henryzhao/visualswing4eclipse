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

import java.awt.Component;
import java.beans.MethodDescriptor;

import org.dyno.visual.swing.editors.actions.AddEventAction;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

/**
 * 
 * EventMethod
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
class EventMethod {
	private MethodDescriptor methodDesc;
	private String name;
	private EventSet parent;
	public EventMethod(MethodDescriptor methodDesc, String name, EventSet eSet){
		this.methodDesc = methodDesc;
		this.name = name;
		this.parent = eSet;
	}
	public EventSet getParent(){
		return parent;
	}
	public String toString(){
		return this.name;
	}
	public void editCode() {
		Component widget = parent.getParent().getWidget();
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(widget);
		AddEventAction action = new AddEventAction(adapter, parent.getEventSet(), methodDesc);
		action.run();
	}
}

