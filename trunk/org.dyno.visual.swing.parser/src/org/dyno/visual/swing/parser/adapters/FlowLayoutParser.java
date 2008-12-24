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

import java.awt.FlowLayout;

import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class FlowLayoutParser extends LayoutParser {
	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		FlowLayout layout = (FlowLayout) layoutAdapter.getContainer().getLayout();
		int hgap = layout.getHgap();
		int vgap = layout.getVgap();
		int alignment = layout.getAlignment();
		String strAlignment = imports.addImport("java.awt.FlowLayout")+".CENTER";
		if (alignment == FlowLayout.CENTER)
			strAlignment = imports.addImport("java.awt.FlowLayout")+".CENTER";
		else if (alignment == FlowLayout.LEADING)
			strAlignment = imports.addImport("java.awt.FlowLayout")+".LEADING";
		else if (alignment == FlowLayout.LEFT)
			strAlignment = imports.addImport("java.awt.FlowLayout")+".LEFT";
		else if (alignment == FlowLayout.RIGHT)
			strAlignment = imports.addImport("java.awt.FlowLayout")+".RIGHT";
		else if (alignment == FlowLayout.TRAILING)
			strAlignment = imports.addImport("java.awt.FlowLayout")+".TRAILING";
		if (hgap != 5 || vgap != 5) {
			return "new "+imports.addImport("java.awt.FlowLayout")+"(" + strAlignment + ", " + hgap + ", " + vgap + ")";
		} else {
			if (alignment != FlowLayout.CENTER) {
				return "new "+imports.addImport("java.awt.FlowLayout")+"(" + strAlignment + ")";
			} else {
				return "new "+imports.addImport("java.awt.FlowLayout")+"()";
			}
		}
	}
}
