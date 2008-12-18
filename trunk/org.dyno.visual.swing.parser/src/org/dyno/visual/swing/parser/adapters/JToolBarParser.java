package org.dyno.visual.swing.parser.adapters;

import java.awt.Component;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JToolBarParser extends CompositeParser {
	@Override
	protected String createGetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.createGetCode(imports));
		CompositeAdapter ca = (CompositeAdapter) adapter;
		int count = ca.getChildCount();
		for (int i = 0; i < count; i++) {
			Component child = ca.getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			String getMethodName = childAdapter.getCreationMethodName();			
			builder.append(getFieldName(ca.getName()) + ".add(");
			builder.append(getMethodName + "());\n");
		}
		return builder.toString();
	}
}
