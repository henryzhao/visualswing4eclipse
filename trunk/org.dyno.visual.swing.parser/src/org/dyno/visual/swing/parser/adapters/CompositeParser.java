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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class CompositeParser extends WidgetParser {

	@Override
	public boolean generateCode(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		int count = ((CompositeAdapter)adapter).getChildCount();
		for (int i = 0; i < count; i++) {
			Component child = ((CompositeAdapter)adapter).getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			IParser parser = (IParser) childAdapter.getAdapter(IParser.class);
			if (parser!=null&&!parser.generateCode(type, imports, monitor))
				return false;
		}
		if (!adapter.isDirty())
			return true;
		return super.generateCode(type, imports, monitor);
	}
	@Override
	protected void genAddCode(ImportRewrite imports, StringBuilder builder) {
		int count = ((CompositeAdapter) adapter).getChildCount();
		for (int i = 0; i < count; i++) {
			Component child = ((CompositeAdapter) adapter).getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			String getMethodName = childAdapter.getCreationMethodName();
			if (!adapter.isRoot())
				builder.append(getFieldName(((CompositeAdapter) adapter).getName()) + ".");
			builder.append("add(" + getMethodName + "());\n");
		}
	}
}
