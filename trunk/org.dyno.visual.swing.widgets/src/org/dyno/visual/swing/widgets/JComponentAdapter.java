package org.dyno.visual.swing.widgets;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JComponentAdapter extends WidgetAdapter {

	protected JComponentAdapter() {
	}

	protected JComponent createWidget() {
		return null;
	}

	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		return null;
	}
}