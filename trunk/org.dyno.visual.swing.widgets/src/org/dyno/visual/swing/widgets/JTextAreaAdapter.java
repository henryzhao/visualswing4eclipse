package org.dyno.visual.swing.widgets;

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

public class JTextAreaAdapter extends J2DTextComponentAdapter {
	public JTextAreaAdapter() {
		super("jTextArea");
	}

	@Override
	protected JTextComponent createTextComponent() {
		return new JTextArea();
	}

}
