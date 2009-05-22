package org.dyno.visual.swing.editors;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

public class DelegateAction extends Action {
	private IAction editorAction;
	private VisualSwingEditor editor;
	public DelegateAction(VisualSwingEditor editor, IAction editorAction){
		this.editor = editor;
		this.editorAction = editorAction;
	}

	public void run(){
		if(editor.isFocusOnDesigner()){
			if(editor.getEditorAction(editorAction.getId())!=null)
				editor.getEditorAction(editorAction.getId()).run();
		}else{
			editorAction.run();
		}
	}
}
