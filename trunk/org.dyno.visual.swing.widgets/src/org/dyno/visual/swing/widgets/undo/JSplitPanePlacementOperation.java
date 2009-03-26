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

package org.dyno.visual.swing.widgets.undo;

import java.awt.Component;

import javax.swing.JSplitPane;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.JSplitPaneAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class JSplitPanePlacementOperation extends AbstractOperation {
	private JSplitPane container;
	private Component child;
	private String newplacement;
	private String oldplacement;
	private JSplitPaneAdapter parent;
	public JSplitPanePlacementOperation(JSplitPane container, Component child, String placement) {
		super(Messages.JSplitPanePlacementOperation_Reposite_Component);
		this.container = container;
		this.child = child;
		this.newplacement = placement;
		this.parent = (JSplitPaneAdapter) WidgetAdapter.getWidgetAdapter(container);
		this.oldplacement = (String) parent.getChildConstraints(child);
	}


	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		if(!newplacement.equals(oldplacement)){
			if(container.getOrientation()==JSplitPane.HORIZONTAL_SPLIT){
				if(newplacement.equals("left")){ //$NON-NLS-1$
					Component comp = container.getLeftComponent();
					container.remove(child);
					if(comp!=null){
						container.remove(comp);
						container.setRightComponent(comp);
					}
					container.setLeftComponent(child);					
				}else{
					Component comp = container.getRightComponent();
					container.remove(child);
					if(comp!=null){
						container.remove(comp);
						container.setLeftComponent(comp);
					}
					container.setRightComponent(child);
				}
			}else{
				if(newplacement.equals("top")){ //$NON-NLS-1$
					Component comp = container.getTopComponent();
					container.remove(child);
					if(comp!=null){
						container.remove(comp);
						container.setBottomComponent(comp);
					}
					container.setTopComponent(child);
				}else{
					Component comp = container.getBottomComponent();
					container.remove(child);
					if(comp!=null){
						container.remove(comp);
						container.setTopComponent(comp);
					}
					container.setBottomComponent(child);
				}
			}
			container.invalidate();
			container.repaint();
			container.doLayout();
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
			if(container.getOrientation()==JSplitPane.HORIZONTAL_SPLIT){
				if(oldplacement.equals("left")){ //$NON-NLS-1$
					Component comp = container.getLeftComponent();
					container.remove(child);
					if(comp!=null){
						container.remove(comp);
						container.setRightComponent(comp);
					}
					container.setLeftComponent(child);					
				}else{
					Component comp = container.getRightComponent();
					container.remove(child);
					if(comp!=null){
						container.remove(comp);
						container.setLeftComponent(comp);
					}
					container.setRightComponent(child);
				}
			}else{
				if(oldplacement.equals("top")){ //$NON-NLS-1$
					Component comp = container.getTopComponent();
					container.remove(child);
					if(comp!=null){
						container.remove(comp);
						container.setBottomComponent(comp);
					}
					container.setTopComponent(child);
				}else{
					Component comp = container.getBottomComponent();
					container.remove(child);
					if(comp!=null){
						container.remove(comp);
						container.setTopComponent(comp);
					}
					container.setBottomComponent(child);
				}
			}
			container.invalidate();
			container.repaint();
			container.doLayout();
			parent.repaintDesigner();
		}
		return Status.OK_STATUS;
	}
}

