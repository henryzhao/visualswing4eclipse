package org.dyno.visual.swing.widgets;

import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JPasswordField;

public class JPasswordFieldAdapter extends TextWidgetAdapter {

	@Override
	protected Class<? extends JComponent> getWidgetClass() {
		return JPasswordField.class;
	}

//	public JComponent cloneWidget() {
//		JComponent clone = new JPasswordField();
//		ArrayList<PropertyGroup> groups = getPropertyGroups();
//		for (PropertyGroup group : groups) {
//			for (Property property : group.getProperties()) {
//				if (property.isValueSet()) {
//					property.cloneProperty(clone);
//				}
//			}
//		}
//		return clone;
//	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = getWidget().getWidth();
		int h = getWidget().getHeight();
		return new Rectangle(0, 0, w, h);
	}

}
