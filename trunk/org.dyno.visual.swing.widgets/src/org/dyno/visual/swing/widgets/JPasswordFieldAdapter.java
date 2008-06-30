package org.dyno.visual.swing.widgets;

import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JPasswordField;

public class JPasswordFieldAdapter extends TextWidgetAdapter {

	@Override
	protected Class<? extends JComponent> getWidgetClass() {
		return JPasswordField.class;
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = getWidget().getWidth();
		int h = getWidget().getHeight();
		return new Rectangle(0, 0, w, h);
	}

}
