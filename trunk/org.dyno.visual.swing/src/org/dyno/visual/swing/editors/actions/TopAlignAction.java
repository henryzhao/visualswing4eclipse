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
 * TopAlignAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class TopAlignAction extends EditorAction {
	private static String TOP_ACTION_ICON = "/icons/top_align.png";

	public TopAlignAction() {
		setId(ALIGNMENT_TOP);
		setText("Top Alignment in Row");
		setToolTipText("Top Alignment in Row");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(TOP_ACTION_ICON));
		setEnabled(false);
	}
}
