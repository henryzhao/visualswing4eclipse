package org.dyno.visual.swing.parser.adapters;

import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;

import org.dyno.visual.swing.parser.spi.IParser;
import org.dyno.visual.swing.plugin.spi.RootPaneContainerAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JInternalFrameParser extends RootPaneContainerParser {

	@Override
	protected String createGetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.createGetCode(imports));
		RootPaneContainerAdapter rootAdapter=(RootPaneContainerAdapter) adapter;
		WidgetAdapter contentAdapter = rootAdapter.getContentAdapter();
		JPanelParser parser = (JPanelParser) contentAdapter.getAdapter(IParser.class);
		if (parser != null)
			parser.genAddComponentCode(imports, builder);
		contentAdapter.detachWidget();
		return builder.toString();
	}
	@Override
	protected JMenuBar getJMenuBar() {
		return ((JInternalFrame)adapter.getWidget()).getJMenuBar();
	}
}
