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

package org.dyno.visual.swing.editors.actions;

import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;

import org.dyno.visual.swing.plugin.spi.IEventListenerModel;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.action.Action;

public class DelEventAction extends Action {
	private EventSetDescriptor eventSet;
	private MethodDescriptor methodDesc;
	private WidgetAdapter adapter;
	public DelEventAction(WidgetAdapter adapter, EventSetDescriptor eventSet,
			MethodDescriptor methodDesc) {		
		super();
		this.adapter = adapter;
		setId(eventSet.getName() + "_" + methodDesc.getName());
		this.eventSet = eventSet;
		this.methodDesc = methodDesc;
		
		IEventListenerModel model = adapter.getEventDescriptor().get(eventSet);
		String methodName = model.getDisplayName(methodDesc);
		setText(methodName);
	}

	public void run() {
		IEventListenerModel model = adapter.getEventDescriptor().get(eventSet);
		if (model != null) {

			if (model.hasMethod(methodDesc)) {
				model.removeMethod(methodDesc);
			}
			if (model.isEmpty()) {
				adapter.getEventDescriptor().remove(eventSet);
			}
			adapter.setDirty(true);
			adapter.changeNotify();
		}
	}
}

