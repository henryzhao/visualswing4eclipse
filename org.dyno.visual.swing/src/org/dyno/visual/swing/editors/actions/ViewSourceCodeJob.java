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

import java.beans.MethodDescriptor;

import org.dyno.visual.swing.editors.VisualSwingEditor;
import org.dyno.visual.swing.plugin.spi.IEventListenerModel;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;


class ViewSourceCodeJob extends Job {
	private VisualSwingEditor editor;
	private MethodDescriptor methodDesc;
	private IEventListenerModel model;

	public ViewSourceCodeJob(VisualSwingEditor editor,
			MethodDescriptor methodDesc, IEventListenerModel model) {
		super(Messages.ViewSourceCodeJob_View_Source_Code);
		this.editor = editor;
		this.methodDesc = methodDesc;
		this.model = model;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		editor.getDisplay().asyncExec(
				new EditEventRunnable(monitor, editor, methodDesc, model));
		return Status.OK_STATUS;
	}
}
