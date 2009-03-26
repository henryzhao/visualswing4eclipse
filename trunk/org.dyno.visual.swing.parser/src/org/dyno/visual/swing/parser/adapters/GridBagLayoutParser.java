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

import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class GridBagLayoutParser extends LayoutParser {

	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		String layoutName = imports.addImport("java.awt.GridBagLayout");
		return "new " + layoutName + "()";
	}

}
