package org.dyno.visual.swing.editors.actions;

import java.beans.MethodDescriptor;

import org.dyno.visual.swing.editors.VisualSwingEditor;
import org.dyno.visual.swing.plugin.spi.IEventListenerModel;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.actions.OrganizeImportsAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

class EditEventRunnable implements Runnable {
	private IProgressMonitor monitor;
	private VisualSwingEditor editor;
	private MethodDescriptor methodDesc;
	private IEventListenerModel model;

	public EditEventRunnable(IProgressMonitor monitor,
			VisualSwingEditor editor, MethodDescriptor methodDesc,
			IEventListenerModel model) {
		this.monitor = monitor;
		this.editor = editor;
		this.methodDesc = methodDesc;
		this.model = model;
	}

	@Override
	public void run() {
		editor.doSave(monitor);
		IEditorPart sourceEditor = editor.openSouceEditor();
		OrganizeImportsAction action = new OrganizeImportsAction(
				sourceEditor.getEditorSite());
		IFileEditorInput file = (IFileEditorInput) sourceEditor
				.getEditorInput();
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file
				.getFile());
		action.run(unit);
		sourceEditor.doSave(monitor);
		model.editMethod(sourceEditor, methodDesc);
	}
}
