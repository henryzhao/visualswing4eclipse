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
import org.dyno.visual.swing.plugin.spi.EditorAction;
/**
 * 
 * CenterAlignAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class CenterAlignAction extends EditorAction {
	private static String CENTER_ACTION_ICON = "/icons/center_align.png";

	public CenterAlignAction() {
		setId(ALIGNMENT_CENTER);
		setText("Center horizontally");
		setToolTipText("Center horizontally");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(CENTER_ACTION_ICON));
		setEnabled(false);
	}
}
