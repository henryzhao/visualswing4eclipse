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

import org.dyno.visual.swing.parser.spi.ILayoutParser;
import org.dyno.visual.swing.parser.spi.IParser;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public abstract class LayoutParser implements ILayoutParser, IAdaptableContext{
	
	public String createCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(layoutAdapter.getContainer());
		if (!layoutAdapter.isDefaultLayout()||layoutAdapter.isPropertyChanged()) {
			String initCode = getInitCode(imports);
			if(initCode!=null)
				builder.append(initCode);
			if (!adapter.isRoot())
				builder.append(adapter.getID() + ".");
			builder.append("setLayout(");
			builder.append(getNewInstanceCode(imports));
			builder.append(");\n");
		}
		CompositeAdapter conAdapter = (CompositeAdapter) adapter;
		int count = conAdapter.getChildCount();
		for (int i = 0; i < count; i++) {
			Component child = conAdapter.getChild(i);
			builder.append(getAddChildCode(layoutAdapter, child, imports));
		}
		return builder.toString();
	}

	protected String getInitCode(ImportRewrite imports) {
		return null;
	}

	protected LayoutAdapter layoutAdapter;
	
	public void setAdaptable(IAdaptable adaptable) {
		this.layoutAdapter = (LayoutAdapter)adaptable;
	}



	protected String getChildConstraints(Component child, ImportRewrite imports) {
		return null;
	}

	protected abstract String getNewInstanceCode(ImportRewrite imports);

	protected String getAddChildCode(LayoutAdapter adapter, Component child, ImportRewrite imports) {
		String constraints = getChildConstraints(child, imports);
		StringBuilder builder = new StringBuilder();
		WidgetAdapter conAdapter = WidgetAdapter.getWidgetAdapter(adapter.getContainer());
		WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
		if (!conAdapter.isRoot()) {
			builder.append(conAdapter.getID() + ".");
		}
		builder.append("add(");
		IParser childParser = (IParser) childAdapter.getAdapter(IParser.class);
		builder.append(childParser.getCreationMethodName()+"()");
		if (constraints != null) {
			builder.append(", " + constraints);
		}
		builder.append(");\n");
		return builder.toString();
	}
}
