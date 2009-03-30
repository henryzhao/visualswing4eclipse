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

package org.dyno.visual.swing.widgets.layout.undo;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class BorderLayoutPlacementOperation extends AbstractOperation {
	private JComponent container;
	private JComponent child;
	private String newplacement;
	private String oldplacement;
	private CompositeAdapter parent;
	public BorderLayoutPlacementOperation(JComponent container, JComponent child, String placement) {
		super(Messages.BorderLayoutPlacementOperation_Reposite_Component);
		this.container = container;
		this.child = child;
		this.newplacement = placement;
		this.parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
		this.oldplacement = (String) parent.getChildConstraints(child);
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		if(!newplacement.equals(oldplacement)){
			BorderLayout layout = (BorderLayout)container.getLayout();
			Component comp = layout.getLayoutComponent(newplacement);
			container.remove(child);
			if(comp!=null){
				container.add(comp, oldplacement);
			}
			container.add(child, newplacement);
			container.doLayout();
			container.invalidate();
			container.repaint();
			WidgetAdapter containerAdapter = WidgetAdapter.getWidgetAdapter(container);
			containerAdapter.setDirty(true);
			containerAdapter.addNotify();
			parent.repaintDesigner();
		}		
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		if(!oldplacement.equals(newplacement)){
			BorderLayout layout = (BorderLayout)container.getLayout();
			Component comp = layout.getLayoutComponent(oldplacement);
			container.remove(child);
			if(comp!=null){
				container.add(comp, newplacement);
			}
			container.add(child, oldplacement);
			container.doLayout();
			container.invalidate();
			container.repaint();
			parent.repaintDesigner();
		}		
		return Status.OK_STATUS;
	}

}

