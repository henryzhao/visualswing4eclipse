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
		super("View Source Code");
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