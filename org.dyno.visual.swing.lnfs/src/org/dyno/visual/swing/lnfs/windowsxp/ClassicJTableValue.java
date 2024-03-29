
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

package org.dyno.visual.swing.lnfs.windowsxp;

import java.awt.Dimension;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class ClassicJTableValue extends WidgetValue {
	private static final long serialVersionUID = 1L;
	public ClassicJTableValue() {
		put("autoCreateColumnsFromModel", true);
		put("visible", true);
		put("verifyInputWhenFocusTarget", true);
		put("editingRow", -1);
		put("rowMargin", 1);
		put("opaque", true);
		put("autoscrolls", true);
		put("rowSelectionAllowed", true);
		put("showHorizontalLines", true);
		put("focusable", true);
		put("enabled", true);
		put("showVerticalLines", true);
		put("updateSelectionOnSort", true);
		put("rowHeight", 16);
		put("alignmentX", 0.5f);
		put("alignmentY", 0.5f);
		put("intercellSpacing", new Dimension(1, 1));
		put("requestFocusEnabled", true);
		put("editingColumn", -1);
		put("autoResizeMode", 2);		
	}
}

