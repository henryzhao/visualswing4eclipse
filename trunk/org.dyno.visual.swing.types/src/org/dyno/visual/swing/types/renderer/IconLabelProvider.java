package org.dyno.visual.swing.types.renderer;

import org.eclipse.jface.viewers.LabelProvider;

public class IconLabelProvider extends LabelProvider{
	@Override
	public String getText(Object element) {
		if (element == null)
			return "null";
		return element.toString();
	}
}
