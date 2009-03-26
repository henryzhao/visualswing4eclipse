
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

package org.dyno.visual.swing.lnfs.meta;

import java.awt.Dimension;

public class MetaJTableValue extends MetaJComponentValue {
	private static final long serialVersionUID = 1L;

	public MetaJTableValue() {
		put("autoCreateColumnsFromModel", true);
		put("editingRow",-1);
		put("rowMargin",1);
		put("opaque", true);
		put("autoscrolls",true);
		put("rowSelectionAllowed", true);
		put("showHorizontalLines", true);
		put("showVerticalLines", true);
		put("updateSelectionOnSort", true);
		put("rowHeight", 16);
		put("intercellSpacing", new Dimension(1, 1));
		put("editingColumn", -1);
		put("autoResizeMode", 2);
		put("doubleBuffered",false);
		put("alignmentX",0.5f);
		put("alignmentY", 0.5f);		
	}
}

