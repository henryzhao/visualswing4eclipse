package org.dyno.visual.swing.editors;

import org.dyno.visual.swing.editors.actions.BottomAlignAction;
import org.dyno.visual.swing.editors.actions.CenterAlignAction;
import org.dyno.visual.swing.editors.actions.CopyAction;
import org.dyno.visual.swing.editors.actions.CutAction;
import org.dyno.visual.swing.editors.actions.DeleteAction;
import org.dyno.visual.swing.editors.actions.DuplicateAction;
import org.dyno.visual.swing.editors.actions.LeftAlignAction;
import org.dyno.visual.swing.editors.actions.MiddleAlignAction;
import org.dyno.visual.swing.editors.actions.PasteAction;
import org.dyno.visual.swing.editors.actions.PreviewAction;
import org.dyno.visual.swing.editors.actions.RightAlignAction;
import org.dyno.visual.swing.editors.actions.SameHeightAction;
import org.dyno.visual.swing.editors.actions.SameWidthAction;
import org.dyno.visual.swing.editors.actions.SelectAllAction;
import org.dyno.visual.swing.editors.actions.SourceViewAction;
import org.dyno.visual.swing.editors.actions.TopAlignAction;
import org.dyno.visual.swing.plugin.spi.EditorAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.EditorActionBarContributor;

public class VisualSwingEditorContributor extends EditorActionBarContributor {
	private EditorAction[] actions = { 
			new CutAction(), 
			new CopyAction(), 
			new PasteAction(), 
			new DuplicateAction(), 
			null, 
			new DeleteAction(),
			new SelectAllAction(), 
			new PreviewAction(),
			new SourceViewAction(),
			null, 
			new LeftAlignAction(), 
			new RightAlignAction(), 
			new CenterAlignAction(), 
			new TopAlignAction(),
			new BottomAlignAction(), 
			new MiddleAlignAction(), 
			null, 
			new SameWidthAction(), 
			new SameHeightAction() 
			};

	public VisualSwingEditorContributor() {
	}

	@Override
	public void init(IActionBars bars) {
		super.init(bars);
		for (EditorAction action : actions) {
			if (action != null && action.isRetargetable())
				bars.setGlobalActionHandler(action.getId(), action);
		}
	}

	@Override
	public void setActiveEditor(IEditorPart targetEditor) {
		VisualSwingEditor vse = (VisualSwingEditor) targetEditor;
		for (EditorAction action : actions) {
			if (action != null) {
				action.setEditor(vse);
			} else {
				vse.addAction(null);
			}
		}
	}

	public void contributeToMenu(IMenuManager manager) {
		IMenuManager menu = manager.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		for (EditorAction action : actions) {
			if (action != null && !action.isRetargetable()) {
				action.addToMenu(menu);
			} else if (action == null) {
				menu.add(new Separator());
			}
		}
	}

	public void contributeToToolBar(IToolBarManager manager) {
		manager.add(new Separator());
		for (EditorAction action : actions) {
			if (action == null) {
				manager.add(new Separator());
			} else {
				manager.add(action);
			}
		}
	}
}
