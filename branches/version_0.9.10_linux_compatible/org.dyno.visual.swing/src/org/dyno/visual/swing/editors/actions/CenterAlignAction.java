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
/**
 * 
 * CenterAlignAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class CenterAlignAction extends EditorAction {
	private static String CENTER_ACTION_ICON = "/icons/center_align.png"; //$NON-NLS-1$

	public CenterAlignAction() {
		setId(ALIGNMENT_CENTER);
		setText(Messages.CenterAlignAction_Center_Alignment);
		setToolTipText(Messages.CenterAlignAction_Center_Alignment);
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(CENTER_ACTION_ICON));
		setEnabled(false);
	}
}

