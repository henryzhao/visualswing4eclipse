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
 * LeftAlignAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class LeftAlignAction extends EditorAction {
	private static String LEFT_ACTION_ICON = "/icons/left_align.png";

	public LeftAlignAction() {
		setId(ALIGNMENT_LEFT);
		setText("Left Alignment in Column");
		setToolTipText("Left Alignment in Column");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(LEFT_ACTION_ICON));
		setEnabled(false);
	}
}

