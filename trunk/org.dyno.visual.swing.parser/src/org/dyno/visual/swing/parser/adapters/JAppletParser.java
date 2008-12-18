package org.dyno.visual.swing.parser.adapters;

import javax.swing.JApplet;
import javax.swing.JMenuBar;

public class JAppletParser extends RootPaneContainerParser {
	@Override
	protected JMenuBar getJMenuBar() {
		JApplet japplet = (JApplet) adapter.getWidget();
		JMenuBar jmb = japplet.getJMenuBar();
		return jmb;
	}
}
