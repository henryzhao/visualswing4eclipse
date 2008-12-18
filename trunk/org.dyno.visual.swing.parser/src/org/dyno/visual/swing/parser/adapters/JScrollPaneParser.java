package org.dyno.visual.swing.parser.adapters;

import java.awt.Component;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JScrollPaneParser extends CompositeParser {

	@Override
	protected String createGetCode(ImportRewrite imports) {
		CompositeAdapter jspa=(CompositeAdapter) adapter;
		StringBuilder builder = new StringBuilder();
		builder.append(super.createGetCode(imports));
		if (jspa.getChildCount() > 0) {
			Component child = jspa.getChild(0);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			String getMethodName = childAdapter.getCreationMethodName();
			builder.append(getFieldName(jspa.getName()) + ".setViewportView(" + getMethodName + "());\n");
		}
		return builder.toString();
	}
}
