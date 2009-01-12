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

import java.awt.Component;

import org.dyno.visual.swing.parser.spi.IParser;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JToolBarParser extends CompositeParser {
	@Override
	protected void genAddCode(ImportRewrite imports, StringBuilder builder) {
		CompositeAdapter ca = (CompositeAdapter) adapter;
		int count = ca.getChildCount();
		for (int i = 0; i < count; i++) {
			Component child = ca.getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			IParser childParser = (IParser) childAdapter.getAdapter(IParser.class);
			String getMethodName = childParser.getCreationMethodName();
			if (!adapter.isRoot())
				builder.append(getFieldName(ca.getID()) + ".");
			builder.append("add(");
			builder.append(getMethodName + "());\n");
		}
	}
}
