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

import java.awt.BorderLayout;
import java.awt.Component;

import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class BorderLayoutParser extends LayoutParser {


	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		BorderLayout layout = (BorderLayout) layoutAdapter.getContainer().getLayout();
		int hgap = layout.getHgap();
		int vgap = layout.getVgap();
		if (hgap != 0 || vgap != 0) {
			return "new " + imports.addImport("java.awt.BorderLayout") + "("
					+ hgap + ", " + vgap + ")";
		} else {
			return "new " + imports.addImport("java.awt.BorderLayout") + "()";
		}
	}

	@Override
	protected String getChildConstraints(Component child, ImportRewrite imports) {
		BorderLayout layout = (BorderLayout) layoutAdapter.getContainer().getLayout();
		String object = (String) layout.getConstraints(child);
		if (object == null)
			return imports.addImport("java.awt.BorderLayout") + ".CENTER";
		else if (object.equals(BorderLayout.CENTER))
			return imports.addImport("java.awt.BorderLayout") + ".CENTER";
		else if (object.equals(BorderLayout.NORTH))
			return imports.addImport("java.awt.BorderLayout") + ".NORTH";
		else if (object.equals(BorderLayout.SOUTH))
			return imports.addImport("java.awt.BorderLayout") + ".SOUTH";
		else if (object.equals(BorderLayout.EAST))
			return imports.addImport("java.awt.BorderLayout") + ".EAST";
		else if (object.equals(BorderLayout.WEST))
			return imports.addImport("java.awt.BorderLayout") + ".WEST";
		return null;
	}
}
