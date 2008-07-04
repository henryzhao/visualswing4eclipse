/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

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
