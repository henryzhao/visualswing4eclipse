package org.dyno.visual.swing.widgets;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JSeparator;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JSeparatorAdapter extends WidgetAdapter {
	private static int VAR_INDEX = 0;

	public JSeparatorAdapter() {
		super("jSeparator" + (VAR_INDEX++));
	}

	@Override
	protected JComponent createWidget() {
		JSeparator separator = new JSeparator();
		Dimension size = new Dimension(100, 10);
		separator.setSize(size);
		separator.doLayout();
		separator.validate();
		return separator;
	}
}
