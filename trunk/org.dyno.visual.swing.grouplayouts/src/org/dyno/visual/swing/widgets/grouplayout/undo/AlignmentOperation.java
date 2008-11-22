/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.widgets.grouplayout.undo;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public abstract class AlignmentOperation extends AbstractOperation {
	protected JComponent container;
	protected List<CompCons> compcons;
	protected List<WidgetAdapter> widgets;
	protected class CompCons {
		Component component;
		Constraints constraints;
	}

	public AlignmentOperation(String name, JComponent container) {
		super(name);
		this.container = container;
		this.widgets = new ArrayList<WidgetAdapter>();
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		List<WidgetAdapter> selected = containerAdapter.getSelectedWidgets();
		for(WidgetAdapter widget:selected){
			widgets.add(widget);
		}
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		GroupLayout layout = (GroupLayout) container.getLayout();
		for (CompCons cons : compcons) {
			layout.setConstraints(cons.component, cons.constraints);
		}
		container.invalidate();
		CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		containerAdapter.doLayout();
		containerAdapter.setDirty(true);
		containerAdapter.repaintDesigner();
		return Status.OK_STATUS;
	}
}
