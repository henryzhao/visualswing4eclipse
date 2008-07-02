package org.dyno.visual.swing.widgets;

import javax.swing.JComponent;
import javax.swing.JToggleButton;

public class JToggleButtonAdapter extends TextWidgetAdapter {

	@Override
	protected Class<? extends JComponent> getWidgetClass() {
		return JToggleButton.class;
	}

}
