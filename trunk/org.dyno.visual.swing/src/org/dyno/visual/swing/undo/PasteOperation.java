/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.undo;

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

public class PasteOperation extends AbstractOperation {
	private List<WidgetAdapter> clipboard;
	private List<WidgetAdapter> copyedList; 
	private CompositeAdapter rootAdapter;
	public PasteOperation(List<WidgetAdapter> clipboard, CompositeAdapter rootAdapter) {
		super("Paste Components");
		this.clipboard = clipboard;
		this.rootAdapter = rootAdapter;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		copyedList = new ArrayList<WidgetAdapter>();
		for (WidgetAdapter childAdapter : clipboard) {
			WidgetAdapter adapter = (WidgetAdapter) childAdapter.clone();
			rootAdapter.addChild(adapter.getWidget());
			copyedList.add(adapter);
		}
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		for (WidgetAdapter childAdapter : copyedList) {
			rootAdapter.removeChild(childAdapter.getWidget());
		}
		rootAdapter.setDirty(true);
		rootAdapter.repaintDesigner();
		rootAdapter.changeNotify();
		return Status.OK_STATUS;
	}

}
