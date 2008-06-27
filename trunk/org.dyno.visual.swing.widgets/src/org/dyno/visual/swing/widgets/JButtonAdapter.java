package org.dyno.visual.swing.widgets;

import javax.swing.JButton;
import javax.swing.JComponent;

public class JButtonAdapter extends TextWidgetAdapter {

	@Override
	protected Class<? extends JComponent> getWidgetClass() {
		return JButton.class;
	}

}
