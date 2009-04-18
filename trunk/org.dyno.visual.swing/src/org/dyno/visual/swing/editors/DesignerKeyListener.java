package org.dyno.visual.swing.editors;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.actions.ActionFactory;

class DesignerKeyListener implements KeyListener {
	private IActionBars actionBars;
	private VisualSwingEditor editor;

	public DesignerKeyListener(VisualSwingEditor editor) {
		this.editor = editor;
		IEditorSite editorSite = editor.getEditorSite();
		actionBars = editorSite.getActionBars();
	}

	public void keyPressed(KeyEvent e) {
		if (e.keyCode == SWT.DEL) {
			String deleteId = ActionFactory.DELETE.getId();
			IAction action = actionBars.getGlobalActionHandler(deleteId);
			if (action.isEnabled() && !editor.getDesigner().isWidgetEditing()) {
				action.run();
			}
			e.doit = false;
		}
	}

	public void keyReleased(KeyEvent e) {
	}
}
