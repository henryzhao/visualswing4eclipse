/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.editors.actions;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.EditorAction;
import org.eclipse.jface.action.IMenuManager;
/**
 * 
 * SourceViewAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class SourceViewAction extends EditorAction {
	private static String SOURCE_ACTION_ID = "/icons/source_view.png";

	public SourceViewAction() {
		setId(SOURCE);
		setText("View Source Code");
		setToolTipText("View Source Code");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(SOURCE_ACTION_ID));
	}

	@Override
	public void run() {
		if (editor == null)
			return;
		editor.openSouceEditor();
	}

	@Override
	public void addToMenu(IMenuManager editMenu) {
		editMenu.insertAfter(PREVIEW, this);
	}
}
