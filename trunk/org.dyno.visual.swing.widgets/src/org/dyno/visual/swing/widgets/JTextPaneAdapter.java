package org.dyno.visual.swing.widgets;

import javax.swing.JTextPane;
import javax.swing.text.JTextComponent;

public class JTextPaneAdapter extends J2DTextComponentAdapter {
	public JTextPaneAdapter() {
		super("jTextPane");
	}

	@Override
	protected JTextComponent createTextComponent() {
		return new JTextPane();
	}

}
