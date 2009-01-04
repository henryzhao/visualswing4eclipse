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

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.JavaUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
/**
 * 
 * NewVisualComponentVizard
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public abstract class NewVisualComponentVizard extends NewElementWizard {
	private static String NEWCLASS_WIZ_ICON = "/icons/newclass_wiz.png"; //$NON-NLS-1$
	private NewComponentPage fPage;
	private boolean fOpenEditorOnFinish;
	private String superClassName;

	public NewVisualComponentVizard(String superClassName) {
		this.superClassName = superClassName;
		setDefaultPageImageDescriptor(VisualSwingPlugin.getSharedDescriptor(NEWCLASS_WIZ_ICON));
		setDialogSettings(getDialogSettings());
		setWindowTitle("New Visual Component"); //$NON-NLS-1$
		fOpenEditorOnFinish = true;
	}

	public void addPages() {
		super.addPages();
		if (fPage == null) {
			fPage = createPage();
			fPage.setWizard(this);
			fPage.init(getSelection());
		}
		addPage(fPage);
	}
	protected abstract NewComponentPage createPage();
	@Override
	public IDialogSettings getDialogSettings() {
		IDialogSettings settings = VisualSwingPlugin.getDefault().getDialogSettings();
		IDialogSettings section = settings.getSection(NewComponentPage.PAGE_NAME);
		if (section == null) {
			section = new DialogSettings(NewComponentPage.PAGE_NAME);
			settings.addSection(section);
		}
		section.put(NewComponentPage.SETTINGS_CREATECONSTR, false);
		section.put(NewComponentPage.SETTINGS_CREATEUNIMPLEMENTED, true);
		section.put(NewComponentPage.SETTINGS_CREATEMAIN, true);
		section.put(NewComponentPage.SETTINGS_SUPERCLASSNAME, superClassName);
		return settings;
	}

	protected boolean canRunForked() {
		return !fPage.isEnclosingTypeSelected();
	}

	protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
		fPage.createType(monitor);
		IResource resource = fPage.getModifiedResource();
		IProject project = resource.getProject();
		IJavaProject javaProject = JavaCore.create(project);
		JavaUtil.setupLayoutLib(javaProject, monitor);
	}

	public boolean performFinish() {
		warnAboutTypeCommentDeprecation();
		boolean res = super.performFinish();
		if (res) {
			IResource resource = fPage.getModifiedResource();
			if (resource != null) {
				selectAndReveal(resource);
				if (fOpenEditorOnFinish) {
					openResource((IFile) resource);
				}
			}
		}
		return res;
	}

	public IJavaElement getCreatedElement() {
		return fPage.getCreatedType();
	}
}

