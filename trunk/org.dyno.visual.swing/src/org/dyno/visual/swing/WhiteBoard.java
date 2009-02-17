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

package org.dyno.visual.swing;

import org.dyno.visual.swing.designer.Event;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.editors.VisualSwingEditor;
import org.eclipse.jdt.core.IJavaProject;

/**
 * 
 * WhiteBoard
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class WhiteBoard {
	private static VisualSwingEditor editor;

	public static IJavaProject getCurrentProject() {
		return editor==null?null:editor.getHostProject();
	}
	public static void setCurrentEditor(VisualSwingEditor e) {
		editor = e;
	}
	public static VisualDesigner getCurrentDesigner(){
		return editor==null?null:editor.getDesigner();
	}
	public static void sendEvent(Event e) {
		if (editor != null)
			editor.onEvent(e);
	}
}
