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
 * TopAlignAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class TopAlignAction extends EditorAction {
	private static String TOP_ACTION_ICON = "/icons/top_align.png"; //$NON-NLS-1$

	public TopAlignAction() {
		setId(ALIGNMENT_TOP);
		setText(Messages.TopAlignAction_Top_Alignment_in_Row);
		setToolTipText(Messages.TopAlignAction_Top_Alignment_in_Row);
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(TOP_ACTION_ICON));
		setEnabled(false);
	}
}

