package org.dyno.visual.swing.parser.adapters;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class JFrameParser extends RootPaneContainerParser {
	@Override
	protected JMenuBar getJMenuBar() {
		JFrame jframe = (JFrame) adapter.getWidget();
		JMenuBar jmb = jframe.getJMenuBar();
		return jmb;
	}
}
