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
package org.dyno.visual.swing.parser.adapters;

import java.awt.GridLayout;

import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class GridLayoutParser extends LayoutParser {

	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		GridLayout layout = (GridLayout) layoutAdapter.getContainer().getLayout();
		int rows = layout.getRows();
		int columns = layout.getColumns();
		int hgap = layout.getHgap();
		int vgap = layout.getVgap();
		String layoutName = imports.addImport("java.awt.GridLayout");
		if (hgap != 0 || vgap != 0) {
			return "new "+layoutName+"(" + rows + ", " + columns + ", " + hgap + ", " + vgap + ")";
		} else {
			if (columns != 0 || rows != 1) {
				return "new "+layoutName+"(" + rows + ", " + columns + ")";
			} else {
				return "new "+layoutName+"()";
			}
		}
	}

}
