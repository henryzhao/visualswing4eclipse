package org.dyno.visual.swing.types.renderer;

import org.dyno.visual.swing.types.TypePlugin;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class BooleanLabelProvider extends LabelProvider {
	private static final String CHECKED_ICON = "/icons/checked.png";
	private static final String UNCHECKED_ICON = "/icons/unchecked.png";

	@Override
	public Image getImage(Object element) {
		if (element != null && element instanceof Boolean)
			return TypePlugin.getSharedImage(((Boolean) element).booleanValue() ? CHECKED_ICON : UNCHECKED_ICON);
		else
			return TypePlugin.getSharedImage(UNCHECKED_ICON);
	}

	@Override
	public String getText(Object element) {
		return null;
	}
}
