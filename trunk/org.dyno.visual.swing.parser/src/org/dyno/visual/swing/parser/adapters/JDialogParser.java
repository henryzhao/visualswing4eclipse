package org.dyno.visual.swing.parser.adapters;

import javax.swing.JDialog;
import javax.swing.JMenuBar;

public class JDialogParser extends RootPaneContainerParser {
	@Override
	protected JMenuBar getJMenuBar() {
		JDialog jdialog = (JDialog) adapter.getWidget();
		JMenuBar jmb = jdialog.getJMenuBar();
		return jmb;
	}

}
