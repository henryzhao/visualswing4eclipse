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

package org.dyno.visual.swing.undo;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class DuplicateOperation extends AbstractOperation {
	private List<Component> selection;
	private List<CompositeAdapter> parents;
	private List<Component> copyedList;
	public DuplicateOperation(List<Component> selection) {
		super("Duplicate Components");
		this.selection = selection;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		parents = new ArrayList<CompositeAdapter>();
		copyedList = new ArrayList<Component>();
		for (Component child : selection) {
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			WidgetAdapter adapter = (WidgetAdapter) childAdapter.clone();
			CompositeAdapter parentAdapter = (CompositeAdapter) childAdapter.getParentAdapter();
			parentAdapter.addChild(adapter.getWidget());
			copyedList.add(adapter.getWidget());
			parents.add(parentAdapter);
			parentAdapter.setDirty(true);
			parentAdapter.changeNotify();
			parentAdapter.repaintDesigner();
		}
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		for (int i=0;i<copyedList.size();i++) {
			Component child = copyedList.get(i);
			CompositeAdapter parentAdapter = parents.get(i);
			parentAdapter.removeChild(child);
			parents.add(parentAdapter);
			parentAdapter.setDirty(true);
			parentAdapter.changeNotify();
			parentAdapter.repaintDesigner();
		}
		return Status.OK_STATUS;
	}

}

