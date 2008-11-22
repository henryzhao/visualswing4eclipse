/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.editors.actions;

import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.util.Map;

import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.editors.VisualSwingEditor;
import org.dyno.visual.swing.plugin.spi.IEventListenerModel;
import org.dyno.visual.swing.plugin.spi.ParserFactory;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

public class AddEventAction extends Action {
	private EventSetDescriptor eventSet;
	private MethodDescriptor methodDesc;
	private WidgetAdapter adapter;
	public AddEventAction(WidgetAdapter adapter, EventSetDescriptor eventSet, MethodDescriptor methodDesc) {		
		super(methodDesc.getDisplayName(), SWT.CHECK);
		this.adapter = adapter;
		this.eventSet = eventSet;
		this.methodDesc = methodDesc;
		setId(eventSet.getName() + "_" + methodDesc.getName());
		IEventListenerModel model = adapter.getEventDescriptor().get(eventSet);
		if (model != null) {
			if (model.hasMethod(methodDesc)) {
				String displayName = model.getDisplayName(methodDesc);
				setText(displayName);
				setChecked(true);
			}
		}
	}

	public void run() {
		Map<EventSetDescriptor, IEventListenerModel> eventDescriptor = adapter.getEventDescriptor();
		IEventListenerModel model = eventDescriptor.get(eventSet);
		if (model == null) {
			model = ParserFactory.getDefaultParserFactory().newModel(adapter, eventSet);
			eventDescriptor.put(eventSet, model);
		}
		if (!model.hasMethod(methodDesc)) {
			model.addMethod(methodDesc);
		}
		adapter.setDirty(true);
		adapter.changeNotify();
		VisualDesigner designer = adapter.getDesigner();
		if (designer != null) {
			VisualSwingEditor editor = designer.getEditor();
			if (editor != null) {
				Job job = new ViewSourceCodeJob(editor, methodDesc, model);
				job.schedule();
			}
		}
	}
}
