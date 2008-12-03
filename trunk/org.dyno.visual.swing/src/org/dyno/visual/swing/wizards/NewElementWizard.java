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

package org.dyno.visual.swing.wizards;

import java.lang.reflect.InvocationTargetException;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
/**
 * 
 * NewElementWizard
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public abstract class NewElementWizard extends Wizard implements INewWizard {
	private IWorkbench fWorkbench;
	private IStructuredSelection fSelection;

	public NewElementWizard() {
		setNeedsProgressMonitor(true);
	}

	protected void openResource(final IFile resource) {
		final IWorkbenchPage activePage = VisualSwingPlugin.getActivePage();
		if (activePage != null) {
			final Display display = getShell().getDisplay();
			if (display != null) {
				display.asyncExec(new Runnable() {
					public void run() {
						try {
							IDE.openEditor(activePage, resource, true);
						} catch (PartInitException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	}

	protected abstract void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException;

	protected ISchedulingRule getSchedulingRule() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	protected boolean canRunForked() {
		return true;
	}

	public abstract IJavaElement getCreatedElement();

	protected void handleFinishException(Shell shell, InvocationTargetException e) {
	}

	public boolean performFinish() {
		IWorkspaceRunnable op = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException, OperationCanceledException {
				try {
					finishPage(monitor);
				} catch (InterruptedException e) {
					throw new OperationCanceledException(e.getMessage());
				}
			}
		};
		try {
			ISchedulingRule rule = null;
			Job job = Job.getJobManager().currentJob();
			if (job != null)
				rule = job.getRule();
			IRunnableWithProgress runnable = null;
			if (rule != null)
				runnable = new WorkbenchRunnableAdapter(op, rule, true);
			else
				runnable = new WorkbenchRunnableAdapter(op, getSchedulingRule());
			getContainer().run(canRunForked(), true, runnable);
		} catch (InvocationTargetException e) {
			handleFinishException(getShell(), e);
			return false;
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	public boolean isDialogEnabled(String key) {
		IDialogSettings settings = getDialogSettings();
		return !settings.getBoolean(key);
	}

	protected void warnAboutTypeCommentDeprecation() {
	}

	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		fWorkbench = workbench;
		fSelection = currentSelection;
	}

	public IStructuredSelection getSelection() {
		return fSelection;
	}

	public IWorkbench getWorkbench() {
		return fWorkbench;
	}

	protected void selectAndReveal(IResource newResource) {
		BasicNewResourceWizard.selectAndReveal(newResource, fWorkbench.getActiveWorkbenchWindow());
	}
}

