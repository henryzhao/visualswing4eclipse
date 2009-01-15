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

import java.util.List;

import org.dyno.visual.swing.adapter.BeanNameValidator;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.IRenamingListener;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class VarChangeOperation extends AbstractOperation {
	private WidgetAdapter adapter;
	private ICellEditorValidator validator;
	private String lastName;
	private String lastLastName;
	public VarChangeOperation(WidgetAdapter adapter) {
		super(Messages.VarChangeOperation_Change_Var);
		this.adapter = adapter;
		this.validator = new BeanNameValidator(adapter);
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		while (true) {
			Shell parent = getCurrentShell();
			VarNameDialog dialog = new VarNameDialog(parent);
			dialog.setPromptMessage(Messages.VarChangeOperation_Enter_New_Name);
			dialog.setInput(adapter.getName());
			if (dialog.open() == Dialog.OK) {
				String name = dialog.getInput();
				String message = validator.isValid(name);
				if (message != null) {
					MessageDialog.openError(parent, Messages.VarChangeOperation_Invalid_Id, message);
				} else {
					this.lastName = adapter.getName();
					this.lastLastName = adapter.getLastName();
					adapter.setName(name);
					adapter.setLastName(lastName);
					if (!adapter.isRoot()) {
						adapter.getParentAdapter().setDirty(true);
					}
					adapter.lockDesigner();
					List<IRenamingListener> listeners = ExtensionRegistry.getRenamingListeners();
					for(IRenamingListener listener:listeners){
						listener.adapterRenamed(adapter.getCompilationUnit(), adapter);
					}
					adapter.changeNotify();
					break;
				}
			} else
				break;
		}
		return Status.OK_STATUS;
	}

	private Shell getCurrentShell() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if(window==null){
			IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
			if(windows!=null&&windows.length>0){
				window=windows[0];
			}
		}
		Shell parent=null;
		if(window!=null)
			parent=window.getShell();
		else
			parent = Display.getDefault().getActiveShell();
		return parent;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		adapter.setName(lastName);
		adapter.setLastName(lastLastName);
		if (!adapter.isRoot()) {
			adapter.getParentAdapter().setDirty(true);
		}
		adapter.lockDesigner();
		List<IRenamingListener> listeners = ExtensionRegistry.getRenamingListeners();
		for(IRenamingListener listener:listeners){
			listener.adapterRenamed(adapter.getCompilationUnit(), adapter);
		}		
		adapter.changeNotify();
		return Status.OK_STATUS;
	}

}

