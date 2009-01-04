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

import org.dyno.visual.swing.base.EditorAction;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
/**
 * 
 * CutAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class CutAction extends EditorAction {
	public CutAction() {
		setId(ActionFactory.CUT.getId());
		setToolTipText(Messages.CutAction_Cut_Components);
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
		setAccelerator(SWT.CTRL | 'X');
		setRetargetable(true);
		setEnabled(false);
	}

	@Override
	public ActionFactory getActionFactory() {
		return ActionFactory.CUT;
	}

}

