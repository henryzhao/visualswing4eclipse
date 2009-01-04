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

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.EditorAction;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IMenuManager;

/**
 * 
 * SourceViewAction
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class SourceViewAction extends EditorAction {
	private static String SOURCE_ACTION_ID = "/icons/source_view.png"; //$NON-NLS-1$

	public SourceViewAction() {
		setId(SOURCE);
		setText(Messages.SourceViewAction_View_Source_Code);
		setToolTipText(Messages.SourceViewAction_View_Source_Code);
		setImageDescriptor(VisualSwingPlugin
				.getSharedDescriptor(SOURCE_ACTION_ID));
	}

	@Override
	public void run() {
		if (editor == null)
			return;
		if (editor.isDirty()) {
			Job job = new Job(Messages.SourceViewAction_View_Source_Code){
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					editor.doSave(monitor);
					return Status.OK_STATUS;
				}
			};
			job.schedule();
			try {job.join();} catch (Exception e) {}
		}
		editor.openSouceEditor();
	}

	@Override
	public void addToMenu(IMenuManager editMenu) {
		editMenu.insertAfter(PREVIEW, this);
	}
}

