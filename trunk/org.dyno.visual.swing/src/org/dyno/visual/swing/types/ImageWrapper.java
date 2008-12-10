
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

package org.dyno.visual.swing.types;

import java.awt.Toolkit;
import java.lang.reflect.Field;
import java.net.URL;

import javax.swing.ImageIcon;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.WhiteBoard;
import org.dyno.visual.swing.base.ResourceImage;
import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class ImageWrapper implements ICodeGen {

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		String rel = null;
		if (value == null) {
			return null;
		} else if (value instanceof ResourceImage) {
			rel = value.toString();
		}
		if (rel != null) {
			Toolkit.getDefaultToolkit().getImage(getClass().getResource(rel));
			String strToolkit = imports.addImport("java.awt.Toolkit");
			return strToolkit + ".getDefaultToolkit().getImage(getClass().getResource(\"" + rel + "\"))";
		} else
			return null;
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}
}

