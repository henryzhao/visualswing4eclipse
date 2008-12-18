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
 * BottomAlignAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class BottomAlignAction extends EditorAction {
	private static String BOTTOM_ACTION_ICON = "/icons/bottom_align.png";

	public BottomAlignAction() {
		setId(ALIGNMENT_BOTTOM);
		setText("Bottom Alignment in Row");
		setToolTipText("Bottom Alignment in Row");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(BOTTOM_ACTION_ICON));
		setEnabled(false);
	}
}

