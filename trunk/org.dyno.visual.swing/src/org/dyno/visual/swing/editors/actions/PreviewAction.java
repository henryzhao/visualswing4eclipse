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
	private static String PREVIEW_ACTION_ICON = "/icons/preview.png"; //$NON-NLS-1$

	public PreviewAction() {
		setId(PREVIEW);
		setText(Messages.PreviewAction_Preview_Design);
		setToolTipText(Messages.PreviewAction_Preview_Design);
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(PREVIEW_ACTION_ICON));
	}

	@Override
	public void addToMenu(IMenuManager editMenu) {
		editMenu.insertAfter(ITextEditorActionConstants.SELECT_ALL, this);
	}
}

