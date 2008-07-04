/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.plugin.spi;

import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.editors.VisualSwingEditor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;
/**
 * 
 * EditorAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public abstract class EditorAction extends Action {

	public static final String ALIGNMENT_BOTTOM = "bottom";
	public static final String ALIGNMENT_CENTER = "center";
	public static final String ALIGNMENT_LEFT = "left";
	public static final String ALIGNMENT_MIDDLE = "middle";
	public static final String ALIGNMENT_RIGHT = "right";
	public static final String ALIGNMENT_TOP = "top";

	public static final String SAME_WIDTH = "same_width";
	public static final String SAME_HEIGHT = "same_height";

	public static final String PREVIEW = "preview";
	public static final String SOURCE = "source";
	public static final String DUPLICATE = "duplicate";

	protected VisualSwingEditor editor;
	private boolean retargetable;

	public EditorAction() {
	}

	public void setEditor(VisualSwingEditor editor) {
		this.editor = editor;
		this.editor.addAction(this);
	}

	public void run() {
		if (editor == null)
			return;
		VisualDesigner designer = editor.getDesigner();
		if (designer == null)
			return;
		if (designer.isWidgetEditing())
			return;
		designer.doAction(this);
	}

	protected void setRetargetable(boolean b) {
		this.retargetable = b;
	}

	public boolean isRetargetable() {
		return retargetable;
	}

	public ActionFactory getActionFactory() {
		return null;
	}

	public void addToMenu(IMenuManager editMenu) {
		editMenu.add(this);
	}
}
