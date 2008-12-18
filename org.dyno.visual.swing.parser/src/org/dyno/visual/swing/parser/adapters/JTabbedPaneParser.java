package org.dyno.visual.swing.parser.adapters;

import java.awt.Component;

import javax.swing.JTabbedPane;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JTabbedPaneParser extends CompositeParser {

	@Override
	protected String createGetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.createGetCode(imports));
		JTabbedPane jtp = (JTabbedPane) adapter.getWidget();
		CompositeAdapter ca=(CompositeAdapter)adapter;
		int count = ca.getChildCount();
		for (int i = 0; i < count; i++) {
			Component child = ca.getChild(i);
			WidgetAdapter childAdapter = WidgetAdapter.getWidgetAdapter(child);
			String getMethodName = childAdapter.getCreationMethodName();
			builder.append(getFieldName(ca.getName()) + ".addTab(");
			String title = jtp.getTitleAt(i);
			builder.append("\"" + title + "\", ");
			builder.append(getMethodName + "());\n");
		}
		return builder.toString();
	}
}
