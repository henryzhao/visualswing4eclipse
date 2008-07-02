package org.dyno.visual.swing.widgets;

import javax.swing.JEditorPane;
import javax.swing.text.JTextComponent;

public class JEditorPaneAdapter extends J2DTextComponentAdapter {
	public JEditorPaneAdapter() {
		super("jEditorPane");
	}

	@Override
	protected JTextComponent createTextComponent() {
		return new JEditorPane();
	}

}
