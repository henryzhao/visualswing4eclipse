
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

package org.dyno.visual.swing.types.endec;

import java.awt.Rectangle;

import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

/**
 * 
 * @author William Chen
 */
public class RectangleWrapper implements ICodeGen {
	
	public String getJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return "null";
		Rectangle bounds = (Rectangle) value;
		String str = imports.addImport("java.awt.Rectangle");
		return "new "+str+"(" + bounds.x + ", " + bounds.y + ", " + bounds.width + ", " + bounds.height + ")";
	}

	
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}
}

