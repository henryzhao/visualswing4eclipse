package org.dyno.visual.swing.types.renderer;

import java.awt.Rectangle;

import org.eclipse.jface.viewers.LabelProvider;

public class RectangleLabelProvider extends LabelProvider {
	@Override
	public String getText(Object element) {
		if (element == null)
			return "null";
		if (element instanceof Rectangle) {
			Rectangle bounds = (Rectangle) element;
			return "(" + bounds.x + ", " + bounds.y + ", " + bounds.width + ", " + bounds.height + ")";
		}
		return element.toString();
	}
}
