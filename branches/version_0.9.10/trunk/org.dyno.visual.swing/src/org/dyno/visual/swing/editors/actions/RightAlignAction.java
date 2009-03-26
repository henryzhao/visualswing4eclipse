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
 * RightAlignAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class RightAlignAction extends EditorAction {
	private static String RIGHT_ACTION_ICON = "/icons/right_align.png"; //$NON-NLS-1$

	public RightAlignAction() {
		setId(ALIGNMENT_RIGHT);
		setText(Messages.RightAlignAction_Right_Alignment_in_Column);
		setToolTipText(Messages.RightAlignAction_Right_Alignment_in_Column);
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(RIGHT_ACTION_ICON));
		setEnabled(false);
	}
}

