package org.dyno.visual.swing.widgets;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class JLabelAdapter extends TextWidgetAdapter {

	@Override
	protected Class<? extends JComponent> getWidgetClass() {
		return JLabel.class;
	}

}
