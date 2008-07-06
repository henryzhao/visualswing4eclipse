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
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
/**
 * 
 * PreviewAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class PreviewAction extends EditorAction {
	private static String PREVIEW_ACTION_ICON = "/icons/preview.png";

	public PreviewAction() {
		setId(PREVIEW);
		setText("Preview Design");
		setToolTipText("Preview Design");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(PREVIEW_ACTION_ICON));
	}

	@Override
	public void addToMenu(IMenuManager editMenu) {
		editMenu.insertAfter(ITextEditorActionConstants.SELECT_ALL, this);
	}
}
