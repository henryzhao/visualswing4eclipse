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
 * MiddleAlignAction
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class MiddleAlignAction extends EditorAction {
	private static String MIDDLE_ACTION_ICON = "/icons/middle_align.png";

	public MiddleAlignAction() {
		setId(ALIGNMENT_MIDDLE);
		setText("Center vertically");
		setToolTipText("Center vertically");
		setImageDescriptor(VisualSwingPlugin.getSharedDescriptor(MIDDLE_ACTION_ICON));
		setEnabled(false);
	}
}
