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
import org.eclipse.swt.SWT;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
/**
 * 
 * DuplicateAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class DuplicateAction extends EditorAction {
	private static String DUPLICATE_ACTION_ICON = "/icons/duplicate.png";

	public DuplicateAction() {
		setId(DUPLICATE);
		setText("Duplicate Components");
		setToolTipText("Duplicate Components");
		setAccelerator(SWT.CTRL | 'D');
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(DUPLICATE_ACTION_ICON));
		setEnabled(false);
	}

	@Override
	public void addToMenu(IMenuManager editMenu) {
		editMenu.insertAfter(ITextEditorActionConstants.PASTE, this);
	}
}
