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

import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;

import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JInternalFrameParser extends RootPaneContainerParser {
	@Override
	protected JMenuBar getJMenuBar() {
		return ((JInternalFrame)adapter.getWidget()).getJMenuBar();
	}
	@Override
	protected void createPostInitCode(StringBuilder builder, ImportRewrite imports) {
		Dimension size = adapter.getWidget().getSize();
		builder.append("setSize(" + size.width + ", "
				+ size.height + ");\n");
	}		
}
